package com.uade.tpo.demo.job;

import com.uade.tpo.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CartCleanupScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(CartCleanupScheduler.class);
    
    @Autowired
    private CartService cartService;
    

    @Scheduled(cron = "*/10 * * * * *")
    public void cleanupExpiredCarts() {
        logger.info("Iniciando limpieza de carritos vencidos...");
        
        try {
            int deactivatedCount = cartService.deactivateExpiredCarts();
            
            if (deactivatedCount > 0) {
                logger.info("Limpieza completada. Carritos inactivados: {}", deactivatedCount);
            } else {
                logger.info("Limpieza completada. No hay carritos para inactivar.");
            }
            
        } catch (Exception e) {
            logger.error("Error durante la limpieza de carritos: ", e);
        }
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void deleteOldInactiveCarts() {
        logger.info("Iniciando eliminación de carritos inactivos antiguos (>30 días)...");

        try {
            int deletedCount = cartService.deleteOldInactiveCarts();

            if (deletedCount > 0) {
                logger.info("Eliminación completada. Carritos eliminados permanentemente: {}", deletedCount);
            } else {
                logger.info("Eliminación completada. No hay carritos antiguos para eliminar.");
            }

        } catch (Exception e) {
            logger.error("Error durante la eliminación de carritos antiguos: ", e);
        }
    }
}
