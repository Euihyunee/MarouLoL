package com.grassparty.tft.Service;

// 병그니 "puuid": "B4ih79LiSu71XEqrD9RyY9RZK5PG5y495cRv9lmGx0emFsYYx32ftAKARxcQ7p4IQM_z-t_pt5qVKA" "counts=q?"
// matchid : "KR_5927958808"

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grassparty.tft.Model.Riot.MatchDto;
import com.grassparty.tft.Model.Riot.MatchID;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchDTOService {

    public MatchID GetMatchIdByPuuid(String puuid){
        String api_query = "&api_key=";
        String api_key = "RGAPI-0c49710a-31e5-4f66-bc0c-887b6da3b71c";
        String site = "https://asia.api.riotgames.com/tft/match/v1/matches/by-puuid/";
        String site_query = "/ids?start=0&count=10";
        String url = site + puuid + site_query + api_query + api_key;
        return GetMatchID(url);
    }

    private MatchID GetMatchID(String matchurl) {
        MatchID matchID = new MatchID();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Riot-Token", "RGAPI-0c49710a-31e5-4f66-bc0c-887b6da3b71c"); //헤더에 API키 추가

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            URI url = URI.create(matchurl);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class); //요청을 보내어 결과를 받아옴

            String matches = response.getBody().toString();
            matches = matches.replace("[", "");
            matches = matches.replace("]", "");
            matches = matches.replace("\"", "");
            String[] matchList = matches.split(",");
            matchID.setMatchid(List.of(matchList));
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");

//            InputStream responseStream = con.getInputStream();
//
//            // Manually converting the response body InputStream to summonerDTO using Jackson
//            ObjectMapper mapper = new ObjectMapper();
//            matchID = mapper.readValue(responseStream, MatchID.class);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return matchID;
    }
    public MatchDto GetMatchDTOByMatchId(String matchid) {
        String api_query = "?api_key=";
        String api_key = "RGAPI-0c49710a-31e5-4f66-bc0c-887b6da3b71c";
        String site = "https://asia.api.riotgames.com/tft/match/v1/matches/";
        String url = site + matchid + api_query + api_key;
        return GetMatchDTO(url);
    }
    @ResponseBody
    private MatchDto GetMatchDTO(String matchurl){
        MatchDto matchDto = null;
        try {
            URL url = new URL(matchurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            InputStream responseStream = con.getInputStream();

            // Manually converting the response body InputStream to summonerDTO using Jackson
            ObjectMapper mapper = new ObjectMapper();
            matchDto = mapper.readValue(responseStream, MatchDto.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matchDto;
    }
}