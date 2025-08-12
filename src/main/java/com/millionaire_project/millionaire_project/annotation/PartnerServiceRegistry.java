package com.millionaire_project.millionaire_project.annotation;

import com.millionaire_project.millionaire_project.annotation.interfaze.PartnerService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PartnerServiceRegistry implements InitializingBean {

    private final ApplicationContext context;
    private final Map<String, PartnerService> codeMap = new HashMap<>();
    private final Map<String, PartnerService> partnerServiceMap = new HashMap<>();

    public PartnerServiceRegistry(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() {
        // Scan all PartnerService beans with @ServiceAPIProvider annotation
        Map<String, PartnerService> beans = context.getBeansOfType(PartnerService.class);

        beans.values().forEach(service -> {
            ServiceProvider annotation = service.getClass().getAnnotation(ServiceProvider.class);
            if (annotation != null) {
                codeMap.put(annotation.partnerCode(), service);
                partnerServiceMap.put(annotation.partnerName().toLowerCase(), service);
            }
        });
    }

    public PartnerService getByPartnerCode(String code) {
        PartnerService  ps = codeMap.get(code.trim().toLowerCase());
        if(ps == null) {
            System.out.println("BNULL");
        }
        return codeMap.get(code.trim().toLowerCase());
    }

    public PartnerService getByPartnerName(String name) {
        return partnerServiceMap.get(name.toLowerCase());
    }
}
