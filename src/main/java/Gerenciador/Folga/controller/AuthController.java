package Gerenciador.Folga.controller;

import Gerenciador.Folga.model.User;
import Gerenciador.Folga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login"; // Retorna o nome do template Thymeleaf
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/funcionarios"; //
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Email ou senha incorretos."); // Mensagem de erro se a autenticação falhar
            return "login"; // Retorna ao formulário de login com erro
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // Adiciona um novo usuário ao modelo para o formulário
        return "register"; // Retorna o nome do template Thymeleaf
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Este e-mail já está cadastrado.");
            return "register"; // Retorna ao formulário se o e-mail já estiver cadastrado
        }

        userService.saveUser(user); // Método para salvar o usuário
        redirectAttributes.addFlashAttribute("registerSuccess", "Registro realizado com sucesso!"); // Mensagem de sucesso
        return "redirect:/auth/login"; // Redireciona após o registro
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model) {
        return "reset-password"; // Retorna o nome do template Thymeleaf
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "As senhas não coincidem."); // Mensagem de erro se as senhas não coincidirem
            return "reset-password"; // Retorna ao formulário se as senhas não coincidirem
        }

        boolean isUpdated = userService.resetPassword(email, newPassword); // Tenta atualizar a senha

        if (isUpdated) {
            model.addAttribute("success", "Senha atualizada com sucesso!"); // Mensagem de sucesso
            return "redirect:/auth/login"; // Redireciona após atualizar a senha
        } else {
            model.addAttribute("error", "Erro ao atualizar a senha. Verifique o email."); // Mensagem de erro se houver erro
            return "reset-password"; // Retorna ao formulário se houver erro
        }
    }
}
