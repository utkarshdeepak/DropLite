package com.utkarshdeepak.droplite.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitialFileRequest {
    private String fileName;
    private int totalChuckCount;
}
