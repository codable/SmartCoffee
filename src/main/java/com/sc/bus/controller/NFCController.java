package com.sc.bus.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ini4j.Ini;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sc.model.NFC;

@Controller
@RequestMapping(value = "/nfc")
public class NFCController {
	
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<NFC> updateNFC(@RequestBody NFC nfc) throws IOException {
		/*
		Ini ini = new Ini();
        ini.load(new FileReader("c:/nfc.txt"));
        
        Ini.Section section = ini.get(nfc.getNfcId());
        if(section == null) {
        	section = ini.add(nfc.getNfcId());
        }
        String cardId = section.get("cardId");
        
        
        if(cardId != null && !cardId.equals(nfc.getCardId())) {
        	
        }
        */
		String data = "[" + nfc.getNfcId() + "]\r\ncardId=" + nfc.getCardId() + "\r\n";
		FileUtils.writeStringToFile(new File("c:/nfc.txt"), data, true);
		
		return new ResponseEntity<NFC>(nfc, HttpStatus.OK);
	}
	
	
	
}
