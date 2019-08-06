package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

import org.codehaus.jackson.map.ObjectMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;

import java.io.IOException;

public class StringToOperationDTO {

    private StringToOperationDTO() {
        // default constructor
    }

    public static OperationDTO convertToOperationDTO(String operationDTO) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(operationDTO, OperationDTO.class);


    }
}
