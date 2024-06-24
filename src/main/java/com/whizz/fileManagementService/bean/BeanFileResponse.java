package com.whizz.fileManagementService.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeanFileResponse {
    String uuid;
    String imageUrl;
}
