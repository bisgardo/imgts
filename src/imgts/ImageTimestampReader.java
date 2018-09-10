package imgts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.TimeZone;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class ImageTimestampReader {
	
	public static Date readTimestamp(String inputName, TimeZone timeZone) throws ImageProcessingException, IOException {
		InputStream is = new FileInputStream(inputName);
		Metadata m = ImageMetadataReader.readMetadata(is);
		ExifSubIFDDirectory d = m.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		return d.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, timeZone);
	}
}
