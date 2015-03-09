import com.polytech4A.pop3.server.core.Connection;

import java.net.Socket;

/**
 * Created by Adrien on 04/03/2015.
 */
public class test {
    public static void main(String[] args) {
        Connection connection = new Connection(new Socket());
    }
}
