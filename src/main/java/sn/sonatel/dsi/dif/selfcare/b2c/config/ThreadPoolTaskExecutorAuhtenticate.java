package sn.sonatel.dsi.dif.selfcare.b2c.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class ThreadPoolTaskExecutorAuhtenticate extends ThreadPoolTaskExecutor {

    public void execute(final Runnable task) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        super.execute(new Runnable() {
            public void run() {
                try {
                    SecurityContext ctx = SecurityContextHolder.createEmptyContext();
                    ctx.setAuthentication(authentication);
                    SecurityContextHolder.setContext(ctx);
                    task.run();
                } finally {
                    SecurityContextHolder.clearContext();
                }
            }
        });
    }
}
