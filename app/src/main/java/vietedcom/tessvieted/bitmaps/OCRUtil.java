package vietedcom.tessvieted.bitmaps;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Smile on 6/22/2016.
 */
public class OCRUtil {
    private static final String LIBRARIES_NAME = "eng.traineddata";
    public static final String LANG = "eng";
    private static final String FOLDER_NAME = "/VietEd/";
    private static final String SUB_PATH = "tessdata/";

    public static String getPathLibraries() {
        return Environment.getExternalStorageDirectory() + FOLDER_NAME;
    }

    public static void moveLibrariesTessToFileDir(@NonNull Context context) throws IOException {
        File folder = new File(getPathLibraries()+SUB_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        final String FILE_PATH = getPathLibraries() +SUB_PATH+ LIBRARIES_NAME;
        if (!(new File(FILE_PATH)).exists()) {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(LIBRARIES_NAME);
            OutputStream out = new FileOutputStream(FILE_PATH);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}
