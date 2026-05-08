package ch.stoeckli.lionel.lostandfound;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.stoeckli.lionel.lostandfound.item.Item;
import ch.stoeckli.lionel.lostandfound.item.ItemRepository;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class ItemControllerTest {

    @Autowired
    private MockMvc api;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetItems() throws Exception {
        String accessToken = obtainAccessToken("user", "user");

        this.itemRepository.save(new Item("Item A", "Beschreibung A", "rot"));

        api.perform(
                get("/api/item")
                        .header("Authorization", "Bearer " + accessToken)
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Item A")));
    }

    @Test
    public void testGetItem() throws Exception {
        String accessToken = obtainAccessToken("user", "user");

        Item newItem = new Item("Item XY", "Beschreibung XY", "blau");
        this.itemRepository.save(newItem);

        String url = String.format("/api/item/%s", newItem.getId());

        api.perform(
                get(url)
                        .header("Authorization", "Bearer " + accessToken)
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Item XY")));
    }

    @Test
    public void testPostItem() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        Item newItem = new Item("Neues Item", "Beschreibung", "grün");

        api.perform(
                post("/api/item")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(newItem))
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Neues Item")));
    }

    @Test
    public void testPutItem() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        Item newItem = new Item("Item XY", "Beschreibung", "schwarz");
        this.itemRepository.save(newItem);

        String url = String.format("/api/item/%s", newItem.getId());

        newItem.setName("Item Neuer Name");

        api.perform(
                put(url)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(newItem))
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Item Neuer Name")));
    }

    @Test
    public void testDeleteItem() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        Item newItem = new Item("Lösch Item", "Beschreibung", "weiss");
        this.itemRepository.save(newItem);

        String url = String.format("/api/item/%s", newItem.getId());

        api.perform(
                delete(url)
                        .header("Authorization", "Bearer " + accessToken)
        )
                .andDo(print()).andExpect(status().isOk());
    }

    private String obtainAccessToken(String username, String password) {

        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=lostandfound&" +
                "grant_type=password&" +
                "scope=openid profile roles offline_access&" +
                String.format("username=%s&", username) +
                String.format("password=%s", password);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> resp = rest.postForEntity(
                "http://localhost:8080/realms/lostandfound/protocol/openid-connect/token",
                entity,
                String.class
        );

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resp.getBody()).get("access_token").toString();
    }
}
