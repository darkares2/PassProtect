package dk.darkares.PassProtect.services;

import dk.darkares.PassProtect.models.KeyStore;
import dk.darkares.PassProtect.repository.KeyStoreRepository;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyService {
    @Autowired
    private KeyStoreRepository keyStoreRepository;

    public KeyStore createKey(KeyStore keyStore) {
        keyStore = keyStoreRepository.save(keyStore);
        keyStoreRepository.flush();
        return keyStore;
    }

    public KeyStore getKeyById(long id) {
        return keyStoreRepository.findById(id).get();
    }

    public List<KeyStore> getAll() {
        return keyStoreRepository.findAll();
    }

    public String generateKey() {
        String foo = "I am a string";
        byte[] bytes = KeyGenerators.secureRandom(64).generateKey();
        return Hex.encodeHexString(bytes);
    }

    public List<KeyStore> getAllByUserId(long userId) {
        return keyStoreRepository.findAllByUserId(userId);
    }

    public void deleteByUserIdAndId(long userId, long id) {
        keyStoreRepository.deleteByUserIdAndId(userId, id);
    }
}
