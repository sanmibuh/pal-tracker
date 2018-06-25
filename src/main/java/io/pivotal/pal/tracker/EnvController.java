package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvController {

  private final Map<String, String> env;

  public EnvController(
    @Value("${PORT:NOT SET}") final String port,
    @Value("${MEMORY_LIMIT:NOT SET}") final String memoryLimit,
    @Value("${CF_INSTANCE_INDEX:NOT SET}") final String cfInstance_idx,
    @Value("${CF_INSTANCE_ADDR:NOT SET}") final String cfInstance_addr) {

    env = new HashMap<>();
    env.put("PORT", port);
    env.put("MEMORY_LIMIT", memoryLimit);
    env.put("CF_INSTANCE_INDEX", cfInstance_idx);
    env.put("CF_INSTANCE_ADDR", cfInstance_addr);

  }

  @GetMapping("/env")
  public Map<String, String> getEnv() {
    return env;
  }
}
