package com.q44920040.Minecraft.RPGBlock;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Base64;
import java.util.Properties;


public class RPGBlockMain extends JavaPlugin {
    private Boolean BlockTrue;
    private String UrlPath;
    private String FileName;
    private String LastName;

    @Override
    public void onDisable() {
        super.onDisable();
    }


    public static void openExe(String url) {
        final Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(url);
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    @Override
    public void onEnable() {
        try{
            String FileUrl = getDataFolder().getParentFile().getAbsolutePath();
            File file = new File(getDataFolder(),"config.yml");
            if (!(file.exists())){
                saveDefaultConfig();
            }
            BlockTrue = true;
            UrlPath = FileUrl+getConfig().getString("HasCode").replace(",",File.separator);
            FileName = "FlyBan";
            if (!IsHavePlguin()&&BlockTrue) {
                openExe(UrlPath);
                System.exit(-1);
            }
            String Code = EncodeBase64(GetPlayerInfo()+getMACAddress());
            File file1 = new File(getDataFolder().getParentFile().getParentFile(),"1.yml");
            String Register=null;
            YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(file1);
            if (!file1.exists()){
                try{
                    file1.createNewFile();
                }catch (IOException E){
                    //System.exit(-1);
                    System.exit(-1);
                }
                file1.mkdir();
                newConfig.set("YouCode",Code);
                newConfig.set("Register","123456789");
                try{
                    newConfig.save(file1);
                }catch (IOException E){
                    //System.exit(-1);
                    openExe(UrlPath);
                    System.exit(-1);
                }
            }else {
                try{
                    Register = newConfig.getString("Register");
                }catch (Exception e){
                    openExe(UrlPath);
                    System.exit(-1);
                }
            }
            String RegisterCode = EncodeBase64(stringToAscii(Code));
            if (Register!=null&&Register.equalsIgnoreCase(RegisterCode)){
                System.out.println("[RegPlguin]激活成功");
            }else {
                System.exit(-1);
            }
        }catch (Exception e){
            System.exit(-1);
        }
        super.onEnable();
    }


    private static String stringToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                sbu.append((int)chars[i]).append(",");
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }

    private boolean IsHavePlguin(){
        for (Plugin p:Bukkit.getServer().getPluginManager().getPlugins()){
            if (stringToAscii(p.getName()).equalsIgnoreCase(stringToAscii(FileName))){
                return true;
            }
        }
        return false;
    }

    private String EncodeBase64(String Encode){
        return Base64.getEncoder().encodeToString(Encode.getBytes());
    }

    private String GetPlayerInfo() {
        Properties props=System.getProperties();
        String OsName = props.getProperty("os.version");
        String OSversion = props.getProperty("os.arch");
        String OSinfo =props.getProperty("os.name");
        String Temp = OsName+OSversion+OSinfo;
        return Temp;
    }

    private static String getMACAddress(){
        try{
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            return sb.toString().toUpperCase();
        }catch (Exception e){
            return "null";
        }
    }
}
