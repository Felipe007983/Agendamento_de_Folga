package Gerenciador.Folga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Gerenciador.Folga.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// Remova ou mantenha métodos conforme sua necessidade
	User findByEmail(String email);
}
