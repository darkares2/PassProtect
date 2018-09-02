package dk.darkares.PassProtect.services;

import dk.darkares.PassProtect.models.Password;
import dk.darkares.PassProtect.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {
    private static final String salt = "4D79566F69636549734D7950617373776F7264";

    @Autowired
    private PasswordRepository passwordRepository;


    public String encryptPassword(String key, String password) {
        return Encryptors.delux(key, salt).encrypt(password);
    }
    public String decryptPassword(String key, String password) {
        return Encryptors.delux(key, salt).decrypt(password);
    }

    public Password createPassword(Password password) {
        return passwordRepository.save(password);
    }

    public Password getPasswordById(long id) {
        return passwordRepository.findById(id).get();
    }

    public Password getPasswordByUserIdAndId(long userId, long id) {
        return passwordRepository.findByIdAndUserId(id, userId);
    }

    public List<Password> getAll() {
        return passwordRepository.findAll();
    }

    public List<Password> getAllByUserId(long userId) {
        return passwordRepository.findAllByUserId(userId);
    }

    public Password getByUserIdAndId(long userId, long passwordId) {
        return passwordRepository.findByIdAndUserId(passwordId, userId);
    }

    public void deleteByUserId(long userId) {
        passwordRepository.deleteByUserId(userId);
    }

    public void deleteByUserIdAndId(long userId, long id) {
        passwordRepository.deleteByUserIdAndId(userId, id);
    }

    public boolean checkKeyUsage(long userId, long id) {
        List<Password> list = passwordRepository.findAllByUserIdAndKeyId(userId, id);
        if (list.size() > 0)
            return true;
        return false;
    }
}
