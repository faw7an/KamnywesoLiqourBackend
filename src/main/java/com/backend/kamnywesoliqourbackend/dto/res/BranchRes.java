package com.backend.kamnywesoliqourbackend.dto.res;

import java.util.UUID;

public record BranchRes(
        UUID id, String name, String location,String managerName, Boolean isHq
) {
}
