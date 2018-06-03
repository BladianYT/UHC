package me.bladian.uhc.util;

import me.bladian.uhc.Core;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BladianYT
 */
public class MySQL
{

    private static Core core = Core.instance;
    private static Reference reference = core.getReference();

    private static List<Connection> connections = new ArrayList<>();

    private static boolean connectionSuccessful;

    public static void openConnection()
    {
        try
        {
            for (int i = 0; i < 3; i++)
            {
                connections.add(DriverManager.getConnection(reference.URL, reference.USER, reference.PASSWORD));
            }
            connectionSuccessful = true;
            Bukkit.getLogger().info("[UHC][MySQL] The connection to MySQL has been made! " + connections.size() + " connections");
        } catch (SQLException e)
        {
            connectionSuccessful = false;
            Bukkit.getLogger().info("[UHC][MySQL] The connection to MySQL couldn't be made! reason: " + e.getMessage());
        }
    }

    private static int i = 0;

    public static Connection getConnection()
    {
        Connection connection = connections.get(i);
        i++;
        if (i < connections.size())
        {
            i = 0;
        }
        if(connection == null)
        {
            return connections.get(i);
        }
        return connection;
    }

    public static void closeConnection()
    {
        for (Connection connection : connections)
        {
            try
            {

                if ((connection != null) && (!connection.isClosed()))
                {
                    connection.close();
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void createTable()
    {
        PreparedStatement statement1 = null;
        try
        {
            Connection connection = MySQL.getConnection();
            assert (connection != null);
            statement1 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS uhc_stats( " +
                    " uuid VARCHAR(36) NOT NULL PRIMARY KEY, " +
                    " wins INT(100) NOT NULL DEFAULT '0', " +
                    " kills INT(100) NOT NULL DEFAULT '0'," +
                    " deaths INT(100) NOT NULL DEFAULT '0'," +
                    " `sword-swings` INT(100) NOT NULL DEFAULT '0'," +
                    " `sword-hits` INT(100) NOT NULL DEFAULT '0'," +
                    " `bow-shots` INT(100) NOT NULL DEFAULT '0'," +
                    " `bow-hits` INT(100) NOT NULL DEFAULT '0'," +
                    " `hearts-dealt` INT(100) NOT NULL DEFAULT '0'," +
                    " `hearts-lost` INT(100) NOT NULL DEFAULT '0'," +
                    " `gapples-eaten` INT(100) NOT NULL DEFAULT '0'," +
                    " `heads-eaten` INT(100) NOT NULL DEFAULT '0'," +
                    "`coal-mined` INT(100) NOT NULL DEFAULT '0'," +
                    " `iron-mined` INT(100) NOT NULL DEFAULT '0'," +
                    " `gold-mined` INT(100) NOT NULL DEFAULT '0'," +
                    " `lapis-mined` INT(100) NOT NULL DEFAULT '0'," +
                    " `redstone-mined` INT(100) NOT NULL DEFAULT '0'," +
                    " `diamond-mined` INT(100) NOT NULL DEFAULT '0'," +
                    " `emerald-mined` INT(100) NOT NULL DEFAULT '0'," +
                    " `blocks-broken` INT(100) NOT NULL DEFAULT '0'," +
                    " `blocks-placed` INT(100) NOT NULL DEFAULT '0'," +
                    " `nethers-entered` INT(100) NOT NULL DEFAULT '0');");
            statement1.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                assert (statement1 != null);
                statement1.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnectionSuccessful()
    {
        return connectionSuccessful;
    }
}
