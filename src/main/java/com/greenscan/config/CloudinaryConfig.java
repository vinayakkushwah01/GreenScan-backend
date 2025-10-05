// package com.greenscan.config;

// import com.cloudinary.Cloudinary;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import java.util.HashMap;
// import java.util.Map;


// @Configuration
// public class CloudinaryConfig {

    
//     @Value("${cloudinary.cloud-name}")
//     private String cloudName;

//     @Value("${cloudinary.api-key}")
//     private String apiKey;

//     @Value("${cloudinary.api-secret}")
//     private String apiSecret;


//     @Bean
//     public Cloudinary getCloudinary() {
//         Map<String, Object> config = new HashMap<>();
//         config.put("cloud_name", this.cloudName);
//         config.put("api_key", this.apiKey);
//         config.put("api_secret", this.apiSecret);
//         config.put("secure", true); 
//         return new Cloudinary(config);
//     }
// }

package com.greenscan.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
