package com.eduquest.backend.application.identity.runner;

import com.eduquest.backend.domain.identity.component.CustomPasswordEncoder;
import com.eduquest.backend.domain.identity.dto.MemberQuery;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.model.Role;
import com.eduquest.backend.domain.identity.model.enums.RoleType;
import com.eduquest.backend.domain.identity.service.MemberCommandService;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.reward.service.WalletCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitAdminRunner implements ApplicationRunner {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final CustomPasswordEncoder passwordEncoder;
    private final WalletCommandService walletCommandService;

    @Value("${admin.id}")
    private String adminId;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 이미 존재하는 Role 이름 목록 조회
        Set<String> existingRoles = memberQueryService.findAllRoles().stream()
                .map(MemberQuery.Role::name)
                .collect(Collectors.toSet());

        // RoleType에 정의된 값 중 DB에 없는 것만 저장
        Arrays.stream(RoleType.values())
                .filter(roleType -> !existingRoles.contains(roleType.name()))
                .forEach(roleType -> memberCommandService.saveRole(Role.of(roleType.name())));

        log.info("Roles have been saved successfully");

        // Admin 계정이 없을 때만 생성
        if (!memberQueryService.isExistByEmail(adminEmail)) {
            Member admin = Member.of(
                    adminId,
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    LocalDate.now(),       // birth
                    "관리자",   // nickname
                    false,      // isLocked
                    0L      // profileId
            );
            memberCommandService.saveMember(admin, RoleType.ADMIN.name());

            // Admin 계정에 대한 wallet 초기화
            UUID savedUuid = memberQueryService.findMemberUuidByUserId(admin.getUserId());
            Long savedMemberId = memberQueryService.findMemberIdByUuid(savedUuid);
            walletCommandService.changeBalance(savedMemberId, 0L, "init-wallet");

            log.info("Admin account has been created successfully");
        } else {
            log.info("Admin account already exists, skipping creation");
        }

    }
}
