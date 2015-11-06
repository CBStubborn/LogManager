package Servlet;

import beans.InitMachine;
import Manager.ConFileProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import SshClient.RemoteConnection;

/**
 * 
 * @author Stubborn
 *
 */
@WebServlet("/LogManagerServlet")
public class LogManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LogManagerServlet() {
		super();
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String operation = req.getParameter("operation");
		switch (operation) {
		case "Add":
			Add(req, resp);
			break;
		case "Delete":
			Delete(req, resp);
			break;
		case "Start": {
			Start();
			break;
		}
		default:
		}
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	public void Add(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<InitMachine> addMachineInfoList = new ArrayList<InitMachine>();
		ConFileProcess aConFileProcess = new ConFileProcess();
		String IpContext = req.getParameter("Ip");
		String RemarksContext = req.getParameter("Remarks");
		String[] IpList = IpContext.split(";；\n\r");
		String[] RemarksList = RemarksContext.split(";");
		for (int i = 0; i < IpList.length; i++) {
			InitMachine im = new InitMachine();
			im.setUserName(req.getParameter("UserName"));
			im.setPassword(req.getParameter("Password"));
			im.setLogPath(req.getParameter("LogPath"));
			im.setRemarks(RemarksList[i]);
			im.setIp(IpList[i]);
			addMachineInfoList.add(im);
		}
		aConFileProcess.addMachineNode(addMachineInfoList);
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	public void Delete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<InitMachine> deleteMachineInfoList = new ArrayList<InitMachine>();
		ConFileProcess aConFileProcess = new ConFileProcess();
		String IpContext = req.getParameter("Ip");
		String RemarksContext = req.getParameter("Remarks");
		String[] IpList = IpContext.split(";；\n\r");
		String[] RemarksList = RemarksContext.split(";");
		for (int i = 0; i < IpList.length; i++) {
			InitMachine im = new InitMachine();
			im.setIp(IpList[i]);
			im.setRemarks(RemarksList[i]);
			im.setLogPath(req.getParameter("LogPath"));
			deleteMachineInfoList.add(im);
		}
		aConFileProcess.deleteMachineNode(deleteMachineInfoList);
	}

	public static void Start() {
		Timer aTimer = new Timer();
		// The program will be executed once a day automatically
		aTimer.schedule(new MyTask(), 0, 24L * 60 * 60 * 1000);
	}
}

/**
 * 
 * @author Stubborn
 *
 */
class MyTask extends TimerTask {
	@Override
	public void run() {
		RemoteConnection.Connection();
	}
}
