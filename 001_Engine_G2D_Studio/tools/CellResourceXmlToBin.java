

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import com.cell.gameedit.object.ImagesSet;
import com.cell.gameedit.object.MapSet;
import com.cell.gameedit.object.SpriteSet;
import com.cell.gameedit.object.WorldSet;
import com.cell.gameedit.object.WorldSet.WaypointObject;
import com.cell.gameedit.output.ConvertXmlToBin;
import com.cell.io.CFile;
import com.cell.io.LittleIODeserialize;
import com.cell.io.LittleIOSerialize;
import com.cell.util.FileFilters;

/**
 * @author zhangyifei
 *
 */
public class CellResourceXmlToBin extends ConvertXmlToBin
{
	public static void main(String ... args)
	{
		ConvertXmlToBin.main(args);
	}
}
