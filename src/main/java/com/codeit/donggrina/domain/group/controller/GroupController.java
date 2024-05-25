package com.codeit.donggrina.domain.group.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.group.dto.request.GroupAppendRequest;
import com.codeit.donggrina.domain.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupController {

  private final GroupService groupService;

  @PostMapping("/my/groups")
  public ApiResponse<Long> append(@RequestBody @Validated GroupAppendRequest request) {
    Long result = groupService.append(request);
    return ApiResponse.<Long>builder()
        .code(HttpStatus.OK.value())
        .message("가족(그룹) 등록 성공")
        .data(result)
        .build();
  }
}
