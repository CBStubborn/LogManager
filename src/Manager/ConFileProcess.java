package Manager;

import beans.InitMachine;
import Manager.ParseConFile;

import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class included three
 * methods:getMachineInfo,addMachineNode,deleteMachineNode
 * 
 * @author Stubborn
 *
 */
public class ConFileProcess {
	private String UserName = null;
	private String Password = null;
	private String Remarks = null;
	private NodeList IpList = null; // All Ip of a MachineInfo Node
	private String Ip = null;
	private String LogPath = null;
	private static Logger log = LoggerFactory.getLogger(ConFileProcess.class);

	/**
	 * 
	 * @return All Machine Information that contains
	 *         UserName,Password,Remarks,Ip,LogPath
	 */
	public ArrayList<InitMachine> getMachineInfo() {
		ArrayList<InitMachine> TargetMachineInfo = new ArrayList<InitMachine>();
		Document aDocument = ParseConFile.getXMLFile();
		NodeList childNodes = aDocument.getElementsByTagName("MachineInfo");
		for (int i = 0; i < childNodes.getLength(); i++) {
			Element MachineInfoElement = (Element) childNodes.item(i);
			Element IpListElement = (Element) MachineInfoElement
					.getElementsByTagName("IpList").item(0);
			UserName = MachineInfoElement.getElementsByTagName("UserName")
					.item(0).getFirstChild().getNodeValue();
			Password = MachineInfoElement.getElementsByTagName("Password")
					.item(0).getFirstChild().getNodeValue();
			LogPath = MachineInfoElement.getElementsByTagName("LogPath")
					.item(0).getFirstChild().getNodeValue();
			Remarks = MachineInfoElement.getElementsByTagName("Remarks")
					.item(0).getFirstChild().getNodeValue();
			IpList = IpListElement.getElementsByTagName("Ip");
			for (int j = 0; j < IpList.getLength(); j++) {
				Ip = IpList.item(j).getFirstChild().getNodeValue();
				InitMachine im = new InitMachine(Ip, UserName, Password,
						LogPath, Remarks);
				TargetMachineInfo.add(im);
			}
		}
		log.info("Return all nodes");
		return TargetMachineInfo;
	}

	/**
	 * 
	 * @param addMachineInfoList contains a lot of machine information  
	 * that will be added to a ConXMLFile         
	 */
	public void addMachineNode(ArrayList<InitMachine> addMachineInfoList) {
		Document aDocument = ParseConFile.getXMLFile();
		NodeList childNodes = aDocument.getElementsByTagName("MachineInfo");
		String addLogPath = addMachineInfoList.get(0).getLogPath();
		//It is used to determine a given LogPath node has exists in ConXMLFile ,or not
		boolean judgeLogPathFlag = true;  
		for (int i = 0; i < childNodes.getLength(); i++) {
			Element MachineInfoElement = (Element) childNodes.item(i);
			LogPath = MachineInfoElement.getElementsByTagName("LogPath")
					.item(0).getFirstChild().getNodeValue();
			if (LogPath.equals(addLogPath)) {
				judgeLogPathFlag = false;
				//It is used to determine a given Ip child node has exists in a LogPath node ,or not
				boolean judgeIpFlag = true; 
				Element IpListElement = (Element) MachineInfoElement
						.getElementsByTagName("IpList").item(0);
				IpList = IpListElement.getElementsByTagName("Ip");
				for (InitMachine addMachineInfo : addMachineInfoList) {
					for (int j = 0; j < IpList.getLength(); j++) {
						Ip = IpList.item(j).getFirstChild().getNodeValue();
						if(Ip.equals(addMachineInfo.getIp())) {
							judgeIpFlag = false;
							log.info("The " + addMachineInfo.getIp() + " has already exist in this LogPath node!");
							break;
						}
					}
					//If judgeIpFlag's value is true ,that means the Ip does not exists
					//in the LogPath node,so it should be added to the LogPath node
					if(judgeIpFlag) {
						String addRemarks = addMachineInfo.getRemarks();
						Remarks = MachineInfoElement.getElementsByTagName("Remarks").item(0)
								.getFirstChild().getNodeValue();
						// update the value of Remarks node
						MachineInfoElement.getElementsByTagName("Remarks").item(0)
								.getFirstChild().setNodeValue(Remarks + "," + addRemarks);
						Element Ip = aDocument.createElement("Ip");
						Ip.appendChild(aDocument.createTextNode(addMachineInfo.getIp()));
						Node IpList = MachineInfoElement.getElementsByTagName("IpList").item(0);
						IpList.appendChild(Ip);
						// update the ConXMLFile
						ParseConFile.updateXMLFile(aDocument);
						log.info("Successed in adding a Ip node to ConXMLFile");
					}
				}
				break;
			}
		}

		//If judgeLogPathFlag's value is true,that means the LogPath does not exist 
		//in the MahineInfo node,so we should create a new MachineInfo node
		if (judgeLogPathFlag) {
			Node LogManager = aDocument.getElementsByTagName("LogManager")
					.item(0);
			Element MachineInfo = aDocument.createElement("MachineInfo");
			Element UserName = aDocument.createElement("UserName");
			Element Password = aDocument.createElement("Password");
			Element Remarks = aDocument.createElement("Remarks");
			Element LogPath = aDocument.createElement("LogPath");
			Element IpList = aDocument.createElement("IpList");
			UserName.appendChild(aDocument.createTextNode(addMachineInfoList
					.get(0).getUserName()));
			Password.appendChild(aDocument.createTextNode(addMachineInfoList
					.get(0).getPassword()));
			LogPath.appendChild(aDocument.createTextNode(addMachineInfoList
					.get(0).getLogPath()));
			Remarks.appendChild(aDocument.createTextNode(addMachineInfoList
					.get(0).getRemarks()));
			for (InitMachine addMachineInfo : addMachineInfoList) {
				Element Ip = aDocument.createElement("Ip");
				Ip.appendChild(aDocument.createTextNode(addMachineInfo.getIp()));
				IpList.appendChild(Ip);
			}
			MachineInfo.appendChild(UserName);
			MachineInfo.appendChild(Password);
			MachineInfo.appendChild(LogPath);
			MachineInfo.appendChild(Remarks);
			MachineInfo.appendChild(IpList);

			LogManager.appendChild(MachineInfo);
			// update the ConXMLFile
			ParseConFile.updateXMLFile(aDocument);
			log.info("Successed in adding a MachineInfo node to ConXMLFile");
		}
	}

	/**
	 * 
	 * @param deleteMachineInfoList that will be deleted from a ConXMLFile
	 *            
	 */
	public void deleteMachineNode(ArrayList<InitMachine> deleteMachineInfoList) {
		Document aDocument = ParseConFile.getXMLFile();
		NodeList childNodes = aDocument.getElementsByTagName("MachineInfo");
		String deleteLogPath = deleteMachineInfoList.get(0).getLogPath();
		//It is used to determine a given LogPath node has exists in ConXMLFile ,or not
		boolean judgeLogPathFlag = true;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Element MachineInfoElement = (Element) childNodes.item(i);
			LogPath = MachineInfoElement.getElementsByTagName("LogPath")
					.item(0).getFirstChild().getNodeValue();
			if (LogPath.equals(deleteLogPath)) {
				judgeLogPathFlag = false;
				Element IpListElement = (Element) MachineInfoElement
						.getElementsByTagName("IpList").item(0);
				IpList = IpListElement.getElementsByTagName("Ip");
				for (InitMachine deleteMachineInfo : deleteMachineInfoList) {
					for (int j = 0; j < IpList.getLength(); j++) {
						Ip = IpList.item(j).getFirstChild().getNodeValue();
						if (Ip.equals(deleteMachineInfo.getIp())) {
							IpListElement.removeChild(IpList.item(j));
							String deleteRemarks = deleteMachineInfo.getRemarks();
							Remarks = MachineInfoElement.getElementsByTagName("Remarks").item(0)
									.getFirstChild().getNodeValue();
							String changedRemarks = Remarks.replace("," + deleteRemarks, "");
							// update the value of Remarks node
							MachineInfoElement.getElementsByTagName("Remarks").item(0).getFirstChild()
									.setNodeValue(changedRemarks);
							break;
						}
					}
				}
				// update the ConXMLFile
				ParseConFile.updateXMLFile(aDocument);
				log.info("Successed in deleting the node from ConXMLFile");
				break;
			}
		}

		if (judgeLogPathFlag) {
			log.error("Does not existence the LogPath,please check your input");
		}
	}
}
