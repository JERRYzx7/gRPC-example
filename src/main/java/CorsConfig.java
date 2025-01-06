import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 配置所有路徑
                        .allowedOrigins("http://localhost:4200")  // 允許的前端來源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")  // 明確列出 HTTP 方法
                        .allowedHeaders("*")  // 允許所有標頭
                        .allowCredentials(true)  // 允許攜帶憑證（如 Cookies）
                        .maxAge(3600);  // 設定 CORS 預檢請求的最大過期時間（秒）
            }
        };
    }
}
