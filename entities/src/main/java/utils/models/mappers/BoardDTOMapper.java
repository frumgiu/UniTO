package utils.models.mappers;

import utils.models.Board;
import utils.models.DTO.BoardDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardDTOMapper {


    public static BoardDTO mapToDTO(Board obj) {
        return new BoardDTO(
                obj.getId(),
                obj.getName(),
                obj.getDescription()
        );
    }

    public static List<BoardDTO> mapAllToDTO(List<Board> obj) {
        //return obj.stream().map(this::mapToDTO).collect(Collectors.toCollection(ArrayList::new));
        return obj.stream().map(BoardDTOMapper::mapToDTO).collect(Collectors.toCollection(ArrayList::new));
    }
}
