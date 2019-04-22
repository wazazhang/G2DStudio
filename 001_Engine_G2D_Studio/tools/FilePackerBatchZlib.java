import java.io.File;

import com.cell.io.CFile;
import com.cell.util.FileFilters;


public class FilePackerBatchZlib 
{

/*
@cd updates__pvr_g3z
@java -classpath ../../lib/g2d_studio.jar -Xmx1024m FilePacker U ..        all/res.mpq all filter_all "pvr.g3z>png;pvr.g3z>jpg"
@java -classpath ../../lib/g2d_studio.jar -Xmx1024m FilePacker U ../g2d_30 030/res.mpq 030 filter_ext "pvr.g3z>png;pvr.g3z>jpg"
@java -classpath ../../lib/g2d_studio.jar -Xmx1024m FilePacker U ../g2d_50 050/res.mpq 050 filter_ext "pvr.g3z>png;pvr.g3z>jpg"
@java -classpath ../../lib/g2d_studio.jar -Xmx1024m FilePacker U ../g2d_60 060/res.mpq 060 filter_ext "pvr.g3z>png;pvr.g3z>jpg"
@FOR /r all %%f IN (*.mpq) DO java -classpath ../../lib/g2d_studio.jar -Xmx1024m ZipFiles A -os "%%f.zip" "%%f" "+.mpq"
@FOR /r 030 %%f IN (*.mpq) DO java -classpath ../../lib/g2d_studio.jar -Xmx1024m ZipFiles A -os "%%f.zip" "%%f" "+.mpq"
@FOR /r 050 %%f IN (*.mpq) DO java -classpath ../../lib/g2d_studio.jar -Xmx1024m ZipFiles A -os "%%f.zip" "%%f" "+.mpq"
@FOR /r 060 %%f IN (*.mpq) DO java -classpath ../../lib/g2d_studio.jar -Xmx1024m ZipFiles A -os "%%f.zip" "%%f" "+.mpq"
@java -classpath ../../lib/g2d_studio.jar -Xmx1024m GenMD5 --md5 -verbos:s -srcDir:all -dstFile:all/update_version.txt -dstEnc:UTF-8 -filter:+.mpq
@java -classpath ../../lib/g2d_studio.jar -Xmx1024m GenMD5 --md5 -verbos:s -srcDir:030 -dstFile:030/update_version.txt -dstEnc:UTF-8 -filter:+.mpq
@java -classpath ../../lib/g2d_studio.jar -Xmx1024m GenMD5 --md5 -verbos:s -srcDir:050 -dstFile:050/update_version.txt -dstEnc:UTF-8 -filter:+.mpq
@java -classpath ../../lib/g2d_studio.jar -Xmx1024m GenMD5 --md5 -verbos:s -srcDir:060 -dstFile:060/update_version.txt -dstEnc:UTF-8 -filter:+.mpq
@cd ..
@java -classpath ../lib/g2d_studio.jar -Xmx1024m GenMD5 --md5 -verbos:s -srcDir:updates__pvr_g3z -dstFile:updates__pvr_g3z/update_version.txt -dstEnc:UTF-8 -filter:+.mpq
*/
		static public void main(String[] args) throws Throwable
		{
			File dst_dir = new File(".", args[3]).getCanonicalFile();
			String dst_ext = args[2].substring(args[2].lastIndexOf("."));
			
			com.cell.filesystem.FileSystem.main(args);
			
			for (File mpq : CFile.listAllChildren(dst_dir, new FileFilters(dst_ext+"$")))
			{
				File mpq_zip = new File(mpq.getPath()+".z");
				ZipFiles.main(new String[]{"D", mpq_zip.getPath(), mpq.getPath()});
				System.out.println("Zip File : " + mpq + " -> .z - " +
						mpq_zip.length() + "/" + mpq.length() + 
						"("+(100*mpq_zip.length()/mpq.length()) + "%)");
			}

			GenMD5.main(new String[]{
					"--md5" ,
					"-verbos:s" ,
					"-srcDir:" + dst_dir.getName(),
					"-dstFile:" + dst_dir.getName()+"/update_version.txt" ,
					"-dstEnc:UTF-8" ,
					"-filter:+"+dst_ext+"$"});
		}
	
}
