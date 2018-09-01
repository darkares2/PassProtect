package dk.darkares.PassProtect;


import dk.darkares.PassProtect.controllers.HomeController;
import dk.darkares.PassProtect.models.Password;
import dk.darkares.PassProtect.models.User;
import dk.darkares.PassProtect.services.KeyService;
import dk.darkares.PassProtect.services.PasswordService;
import dk.darkares.PassProtect.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PasswordTests {
    @Autowired
    private KeyService keyService;
    @Autowired
    private PasswordService passwordService;


    @Test
    public void createAndGet() {
        // Given
        Password password = new Password();
        password.setName("somewhere.com");
        password.setUserId(1);
        password.setDescription("Instruktions");
        password.setPassword(passwordService.encryptPassword(keyService.generateKey(), "secret"));
        password = passwordService.createPassword(password);

        // When
        Password found = passwordService.getPasswordById(password.getId());

        // THen
        assertThat(password.getName()).isEqualTo(found.getName());
    }
    @Test
    public void getAll() {
        // Given
        create10Passwords(1);

        // When
        List<Password> all = passwordService.getAll();

        // then
        assertThat(all.size())
                .isEqualTo(10);
    }


    @Test
    public void encryptDecrypt() {
        // given
        String password = "Fun!And?Games%_12345";
        String key = keyService.generateKey();
        String encryptedPassword = passwordService.encryptPassword(key, password);

        // when
        String decryptedPassword = passwordService.decryptPassword(key, encryptedPassword);

        // then
        assertThat(password)
                .isEqualTo(decryptedPassword);
    }

    @Autowired
    Environment environment;
    @LocalServerPort
    String port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Before
    public void clean() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"User", "Password");
    }

    @Test
    public void controllerGetAll() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        User user = login("test", "test", headers);
        create10Passwords(user.getId());

        // When
        String url = "http://localhost:" + port + "/api/v1/password/all";
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Password>> response =
                restTemplate.exchange(url,
                        HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Password>>() {
                        });
        List<Password> all = response.getBody();

        // Then
        assertThat(all.size())
                .isEqualTo(10);
    }

    @Test
    public void controllerCreateAndGet() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        User user = login("test", "test", headers);
        Password password = new Password();
        password.setName("Fun");
        password.setKeyId(1);
        password.setPassword("secret");
        password.setDescription("Desc");
        HttpEntity<Password> requestEntity = new HttpEntity<>(password, headers);
        String createUrl = "http://localhost:" + port + "/api/v1/password/";
        ResponseEntity<Password> createResponse =
                restTemplate.exchange(createUrl,
                        HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Password>() {
                        }, String.class);
        Password created = createResponse.getBody();
        assertThat(created.getId()).isNotEqualTo(0);

        // When
        String url = "http://localhost:" + port + "/api/v1/password/" + created.getId();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Password> response =
                restTemplate.exchange(url,
                        HttpMethod.GET, entity , new ParameterizedTypeReference<Password>() {
                        });
        Password found = response.getBody();

        // Then
        assertThat(password.getName())
                .isEqualTo(found.getName());
    }

    @Autowired
    private UserService userService;
    @PersistenceContext
    private EntityManager entityManager;

    private User login(String username, String password, HttpHeaders headers) {
        User user = new User();
        user.setName("test");
        user.setPassword(passwordEncoder.encode("test"));
        user = userService.createUser(user);
        //entityManager.flush();
        String loginUrl = "http://localhost:" + port + "/loginTest";

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String login = "{\"username\":\"test\",\"password\":\"test\"}";
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(login, loginHeaders);

/*        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("username", username);
        map.add("password", password);
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, loginHeaders);*/

        ResponseEntity<HomeController.LoginResponse> loginResponse =
                restTemplate.exchange(loginUrl,
                        HttpMethod.POST, entity, new ParameterizedTypeReference<HomeController.LoginResponse>() {
                        }, String.class);
        assertThat(loginResponse.getBody().getCode()).isEqualTo(200);
        loginHeaders = loginResponse.getHeaders();

        headers.add("Cookie", loginHeaders.get("Set-Cookie").get(0));


        return user;
    }

    private void create10Passwords(long userId) {
        for(int idx = 1;idx<=10;++idx) {
            Password password = new Password();
            password.setName("somewhere.com" + idx);
            password.setUserId(userId);
            password.setDescription("Instruktions" + idx);
            password.setPassword(passwordService.encryptPassword(keyService.generateKey(), "secret"+idx));
            passwordService.createPassword(password);
        }
    }


}
