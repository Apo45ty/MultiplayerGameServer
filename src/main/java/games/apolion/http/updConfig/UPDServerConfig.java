package games.apolion.http.updConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "apolion.games")
public class UPDServerConfig {
	private int UDPbaseport;
	private int UDPmessagebuffersize=1024;
	
	public UPDServerConfig() {
		// TODO Auto-generated constructor stub
	}
	public int getUDPbaseport() {
		return UDPbaseport;
	}
	public void setUDPbaseport(int uDPbaseport) {
		UDPbaseport = uDPbaseport;
	}
	public int getUDPmessagebuffersize() {
		return UDPmessagebuffersize;
	}
	public void setUDPmessagebuffersize(int uDPmessagebuffersize) {
		UDPmessagebuffersize = uDPmessagebuffersize;
	}
	
	
}
