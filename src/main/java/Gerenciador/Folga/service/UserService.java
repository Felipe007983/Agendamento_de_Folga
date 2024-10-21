package Gerenciador.Folga.service;

import Gerenciador.Folga.model.User;
import Gerenciador.Folga.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Se você estiver usando PasswordEncoder

    @Transactional
    public void saveUser(User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Usuário, email e senha não podem ser nulos."); // Verificação de nulos
        }

        // Criptografa a senha antes de salvá-la
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio."); // Verificação de nulo ou vazio
        }

        return userRepository.findByEmail(email);
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder; // Método para obter o PasswordEncoder
    }

    @Transactional
    public boolean resetPassword(String email, String newPassword) {
        if (email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Email e nova senha não podem ser nulos ou vazios."); // Verificação de nulos
        }

        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Atualiza a senha do usuário com criptografia
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true; // Senha atualizada com sucesso
        }
        return false; // Se o usuário não for encontrado
    }
}
