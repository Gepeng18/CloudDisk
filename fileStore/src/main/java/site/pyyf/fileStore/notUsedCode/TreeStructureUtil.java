package site.pyyf.fileStore.notUsedCode;//package site.pyyf.cloudDisk.utils;
//
//public class TreeStructureUtil {
//    public static List<DocumentListVo> getVolists(List<DirectoryStructure> dirlist, List<document> doclist){
//        List<DocumentListVo> listvo = new ArrayList<>();
//
//        List<DirectoryStructure> dirlistPen = new ArrayList<>();//一级目录
//
//        //获取没有目录的文件，跟一级目录同级
//        for(int i=0;i<doclist.size();i++) {
//            document d = doclist.get(i);
//            if (d.getdSId() == null) {
//                DocumentListVo docvo = new DocumentListVo();
//                docvo.setName(d.getName());
//                docvo.setHashCode(d.getHashCode());
//                docvo.setUpdateTime(d.getUpdateTime());
//                listvo.add(docvo);
//            }
//        }
//
//        if(dirlist.size() > 0){
//            //获取一级目录
//            for(int j=0;j<dirlist.size();j++){
//                DirectoryStructure dir = dirlist.get(j);
//                if(dir.getPid() == null){
//                    dirlistPen.add(dir);
//                }
//            }
//
//            //根据一级目录获取子目录和文件
//            if(dirlistPen.size() > 0){
//                for(int k = 0;k<dirlistPen.size();k++){
//                    DirectoryStructure dir = dirlist.get(k);
//                    DocumentListVo docvo = new DocumentListVo();
//                    docvo.setName(dir.getName());
//                    List<DocumentListVo> d =  getVoDrenlist(dir.getId(),dirlist,doclist);//子目录
//                    if(d.size() < 1){
//                        for(int i = 0;i<doclist.size();i++){
//                            document doc = doclist.get(i);
//                            if(doc.getdSId() == dir.getId()){
//                                DocumentListVo docvo1 = new DocumentListVo();
//                                docvo1.setName(doc.getName());
//                                docvo1.setHashCode(doc.getHashCode());
//                                docvo1.setUpdateTime(doc.getUpdateTime());
//                                d.add(docvo1);
//                            }
//                        }
//                    }
//                    docvo.setList(d);
//                    listvo.add(docvo);
//                }
//            }
//        }
//        return listvo;
//    }
//
//    /**
//     * 递归算法获取子目录和文件
//     * @param id
//     * @param dirlist
//     * @param doclist
//     * @return
//     */
//    public static List<DocumentListVo> getVoDrenlist(Long id ,List<DirectoryStructure> dirlist,List<document> doclist){
//        List<DocumentListVo> listvo = new ArrayList<>();
//        for(int j=0;j<dirlist.size();j++){
//            DirectoryStructure dir = dirlist.get(j);
//            if(dir.getPid() == id){
//                DocumentListVo docvo = new DocumentListVo();
//                docvo.setName(dir.getName());
//                docvo.setList(getVoDrenlist(dir.getId(),dirlist,doclist));
//                listvo.add(docvo);
//            }
//            //循环完当前目录级，去看当前级下面有没有文件
//            if(j == (dirlist.size()-1)){
//                for(int i = 0;i<doclist.size();i++){
//                    document doc = doclist.get(i);
//                    if(doc.getdSId() == id){
//                        DocumentListVo docvo1 = new DocumentListVo();
//                        docvo1.setName(doc.getName());
//                        docvo1.setHashCode(doc.getHashCode());
//                        docvo1.setUpdateTime(doc.getUpdateTime());
//                        listvo.add(docvo1);
//                    }
//                }
//            }
//        }
//        return listvo;
//    }
//
//}
//
