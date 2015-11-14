package com.sc.bus.controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
		String nfcId = nfc.getNfcId();
		String cardId = nfc.getCardId();
		if(nfcId.equals("") || cardId.equals("")) {
			return new ResponseEntity<NFC>(nfc, HttpStatus.BAD_REQUEST);
		}
		Ini ini = new Ini();
		File file = new File("c:/nfc.txt");
        ini.load(new FileReader(file));
        
        Ini.Section section = ini.get(nfcId);
        if(section == null) {
        	section = ini.add(nfcId);
        }
        String iniCardId = section.get("cardId");
        if(iniCardId != null && iniCardId.equals(cardId)) {
        	System.out.println("Do nothing");
        	return new ResponseEntity<NFC>(nfc, HttpStatus.OK);
        }
        section.put("cardId", cardId);
        ini.store(new FileWriter(file));
		
		return new ResponseEntity<NFC>(nfc, HttpStatus.OK);
	}
	
	
	
}
