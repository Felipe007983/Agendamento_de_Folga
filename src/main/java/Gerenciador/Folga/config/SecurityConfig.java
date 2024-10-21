package Gerenciador.Folga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable() // Desabilitar CSRF se você não precisar dele para chamadas de API REST; caso contrário, habilite conforme necessário
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/auth/login",
                                "/auth/register",
                                "/auth/reset-password",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll() // Permite acesso sem autenticação a páginas específicas
                        .requestMatchers("/funcionarios/agendar-folga").authenticated() // Requer autenticação para agendar folga
                        .requestMatchers("/funcionarios/folgas-agendadas").authenticated() // Requer autenticação para visualizar folgas agendadas
                        .requestMatchers("/funcionarios/portal-bi").authenticated() // Requer autenticação para acessar o Portal BI
                        .anyRequest().authenticated() // Todas as outras requisições requerem autenticação
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // URL da página de login
                        .loginProcessingUrl("/auth/login") // URL para processamento do login
                        .usernameParameter("email") // Parâmetro do email para login
                        .passwordParameter("password") // Parâmetro da senha para login
                        .defaultSuccessUrl("/funcionarios/portal-bi", true) // URL padrão após sucesso no login
                        .failureUrl("/auth/login?error=true") // URL em caso de falha no login
                        .permitAll() // Permite acesso à página de login
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // URL para logout
                        .logoutSuccessUrl("/auth/login?logout=true") // URL após logout bem-sucedido
                        .permitAll() // Permite acesso à URL de logout
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()); // Configura o codificador de senhas
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usar BCrypt para codificação de senhas
    }
}
