package com.grassparty.tft.Service.RepositoryService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grassparty.tft.Model.FullRecordDTO;
import com.grassparty.tft.Model.Riot.MatchID;
import com.grassparty.tft.Model.domain.FullRecordDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FullRecordRepositoryCreate {

    @Autowired
    FullRecordRepository fullRecordRepository;

    public void InsertFullRecords(FullRecordDTO[] fullRecordDTOS){
        for(FullRecordDTO fullRecordDTO : fullRecordDTOS){
            InsertFullRecord(fullRecordDTO);
        }
    }

    public void InsertFullRecord(FullRecordDTO fullRecordDTO){
        String matchid = fullRecordDTO.getMatch_id();

        Gson gson = new Gson();
        String jsonString = gson.toJson(fullRecordDTO);

        FullRecordDB fullRecordDB = FullRecordDB.builder()
                .matchID(matchid)
                .json(jsonString)
                .build();
        fullRecordRepository.save(fullRecordDB);
    }

    public boolean IsExistByMatchid(String matchid){

        return fullRecordRepository.existsById(matchid);
    }

    public FullRecordDTO[] GetFullRecordDTOsByMatchidFromRepository(MatchID matchID){
        FullRecordDTO[] fullRecordDTOS = new FullRecordDTO[10];
        for(int i=0; i<matchID.getMatchid().length;i++){
            fullRecordDTOS[i] = GetFullRecordDTOFromRepository(matchID.getMatchid()[i]);
        }
        return fullRecordDTOS;
    }

    public FullRecordDTO GetFullRecordDTOFromRepository(String matchid){

        // DB에서 FullRecordDB 가져오기
        Optional<FullRecordDB> fullRecordDB =  fullRecordRepository.findById(matchid);

        // FullRecordDB에서 json 받아오기
        String json =  fullRecordDB.get().getJson();

        // Gson 역직렬화까즤
        Gson gson = new Gson();
        FullRecordDTO fullRecordDTO = gson.fromJson(json, FullRecordDTO.class);

        return fullRecordDTO;
    }

}
