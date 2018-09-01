package dk.darkares.PassProtect;


import dk.darkares.PassProtect.models.KeyStore;
import dk.darkares.PassProtect.services.KeyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeyStoreTests {
    @Autowired
    private KeyService keyService;

    @Test
    public void createAndFindKey() {
        // given
        KeyStore newKeyStore = new KeyStore();
        newKeyStore.setName("My key");
        newKeyStore.setkeyContent(keyService.generateKey());
        newKeyStore.setUserId(1);
        newKeyStore = keyService.createKey(newKeyStore);

        // when
        KeyStore found = keyService.getKeyById(newKeyStore.getId());

        // then
        assertThat(found.getName())
                .isEqualTo(newKeyStore.getName());
        assertThat(found.getkeyContent().length()).isEqualTo(128);
    }

    @Test
    public void listKeys() {
        // given
        for(int idx = 1;idx<=10;++idx) {
            KeyStore newKeyStore = new KeyStore();
            newKeyStore.setName("My key " + idx);
            newKeyStore.setkeyContent(keyService.generateKey());
            newKeyStore.setUserId(1);
            newKeyStore = keyService.createKey(newKeyStore);
        }

        // when
        List<KeyStore> all = keyService.getAll();

        // then
        assertThat(all.size())
                .isEqualTo(10);
    }

}
