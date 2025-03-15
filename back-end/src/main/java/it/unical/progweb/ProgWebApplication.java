package it.unical.progweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = {"it.unical.progweb", "it.unical.progweb.services"})
public class ProgWebApplication {
    public static void main(String[] args) {
        System.out.println("\n\n✅✅✅ Avvio di Spring Boot... ✅✅✅\n\n");

        ApplicationContext context = SpringApplication.run(ProgWebApplication.class, args);

        System.out.println("\n\n✅✅✅ Spring Boot è stato avviato! ✅✅✅\n\n");

        // Stampiamo tutti i bean registrati
        String[] beanNames = context.getBeanDefinitionNames();
        System.out.println("\n\n🛠 BEAN REGISTRATI DA SPRING BOOT: 🛠");
        for (String beanName : beanNames) {
            System.out.println("🔹 " + beanName);
        }
        System.out.println("\n\n");
    }
}