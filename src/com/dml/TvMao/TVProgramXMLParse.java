package com.dml.TvMao;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;



public class TVProgramXMLParse {

	public List<OneProgramInfo> parse(String xmlStr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<OneProgramInfo> programInfos = new ArrayList<OneProgramInfo>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();

			ResultContextHandler resultContentHandler = new ResultContextHandler(
					programInfos);

			xmlReader.setContentHandler(resultContentHandler);

			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			
			
			for (Iterator iterator = programInfos.iterator(); iterator
					.hasNext();) {
				OneProgramInfo programInfo = (OneProgramInfo) iterator.next();
				
				//System.out.println(programInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return programInfos;

	}

	private class ResultContextHandler extends DefaultHandler {
		private List<OneProgramInfo> programInfos = null;
		private OneProgramInfo oneProgramInfo = null;
		private String tagName = null;

		public ResultContextHandler(List<OneProgramInfo> programInfos) {
			super();
			this.programInfos = programInfos;
		}

		public List<OneProgramInfo> getInfos() {
			return programInfos;
		}

		public void setInfos(List<OneProgramInfo> infos) {
			this.programInfos = programInfos;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String temp = new String(ch, start, length);
			if(tagName.equals("id")){
				oneProgramInfo.setId(temp);
			}else if (tagName.equals("title")) {
				oneProgramInfo.setTitle(temp);
			} else if (tagName.equals("channel")) {
				oneProgramInfo.setChannel(temp);
			} else if (tagName.equals("playtime")) {
				oneProgramInfo.setPlaytime(temp);
			} else if (tagName.equals("playweek")){
				oneProgramInfo.setPlayweek(temp);
			}else if (tagName.equals("playdate")){
				oneProgramInfo.setPlaydate(temp);
			}else if (tagName.equals("subinfo")) {
				oneProgramInfo.setSubinfo(temp);
			}
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (qName.equals("ProgramInfo")) {
				programInfos.add(oneProgramInfo);
			}
			tagName = "";
		}

		public void startDocument() throws SAXException {
			super.startDocument();
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			this.tagName = localName;
			if (tagName.equals("ProgramInfo")) {
				oneProgramInfo = new OneProgramInfo();
			}
		}
	}
}
