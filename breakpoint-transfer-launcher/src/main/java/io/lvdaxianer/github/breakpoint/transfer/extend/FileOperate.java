package io.lvdaxianer.github.breakpoint.transfer.extend;

import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileOperate {
  ResponseEntity upload(MultipartFile file, String baseDir, String filename);

  ResponseEntity verify(String filename);

  ResponseEntity list(String baseDir);

  ResponseEntity merge(String baseDir, String filename);
}
