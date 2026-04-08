package com.facet.api.ask;


import com.facet.api.ask.model.Ask;
import com.facet.api.ask.model.AskDto;
import com.facet.api.user.model.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AskService {
    private final AskRepository askRepository;

    public AskDto.RegRes reg(AskDto.RegReq dto) {
        Ask ask = dto.toEntity();
        Ask result = askRepository.save(ask);

        return AskDto.RegRes.from(result);

    }


    public List<AskDto.listRes> list(String userEmail) {
        List<Ask> result = askRepository.findByEmail(userEmail);

        return result.stream()
                .map(AskDto.listRes::from)
                .collect(Collectors.toList());
    }
}