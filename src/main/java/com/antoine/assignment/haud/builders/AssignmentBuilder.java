package com.antoine.assignment.haud.builders;

import com.antoine.assignment.haud.dtos.SimDTO;
import com.antoine.assignment.haud.models.SimModel;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AssignmentBuilder {
    private final ModelMapper mapper;

    @Autowired
    public AssignmentBuilder(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public List<SimDTO> buildSimList(List<SimModel> models) {

        List<SimDTO> dtoList = new ArrayList<>();
        models.forEach(model -> {
            SimDTO dto = mapper.map(model, SimDTO.class);
            log.info("adding in the response list the following dto : [{}] ", dto.toString());
            dtoList.add(dto);
        });

        return dtoList;
    }
}
