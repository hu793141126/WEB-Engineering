package DAO;
import java.util.*;
import javax.sql.*;
import java.io.*;
import org.springframework.jdbc.core.*;
import org.springframework.transaction.*;
import org.springframework.jdbc.datasource.*;
import org.springframework.transaction.support.*;            

import Entity.*;

public class DBUtil{
	private JdbcTemplate jt;									//����JdbcTemplate��������						
	private List rl = null;										//����List��������
	private String sql = null;								//����SQL�ַ�������
	private DataSource ds;										//����DataSource����
	private DataSourceTransactionManager dtm;				
	private DefaultTransactionDefinition dtd;
	public void setJt(JdbcTemplate jt){						//jt��Ա��setter����
		this.jt = jt;										//����jt���Ե�ֵ
	}
	public void setDs(DataSource ds){
		this.ds=ds;
	}
	
	public List getIndexContent(String sql){
		List al = new ArrayList();							//��������List����
		try{
			rl = jt.queryForList(sql);							//�����õ����
			if(rl.size() != 0){									//�����Ϊ�յ����
				for(int i = 0;i<rl.size();i++){					//�������
					Map map=(Map)rl.get(i);						//���List��ÿһ��Ϊһ��LinkedHashMap
					GroupItem gi = new GroupItem();				//����һ��GroupItem����
					gi.setGid(map.get("gid").toString());		//���ÿγ�IDֵ
					gi.setGName(new String(map.get("gname").toString()));
					gi.setDetail(new String(map.get("detail").toString()));
					gi.setTopic(map.get("topic").toString());	//����������������ֵ
					gi.setRevert(map.get("revert").toString());	//���ûظ���������ֵ
					gi.setUname(new String(map.get("uname").toString()));
					gi.setTid((String) map.get("tid"));	//�������ظ������IDֵ
					gi.setTitle(new String(map.get("title").toString()));
					gi.setLastTime((String) map.get("lastTime"));  
					
					al.add(gi);									//���˿γ̶���Ž�����List��
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;														//���ؽ��
	}
	
	public List getGroupContent(String sql){	//�õ��������
		List al = new ArrayList();							//��������List����		
		try{
			rl = jt.queryForList(sql);						//�����õ����
			if(rl.size()!=0){								//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){				//�������
					Map map=(Map)rl.get(i);					//���List��ÿһ��Ϊһ��LinkedHashMap
					TopicItem ti = new TopicItem();			//����һ��TopicItem����
					ti.setTid(map.get("tid").toString());	//����TopicItem�����������������ֵ
					ti.setFtr(new String(map.get("ftr").toString()));
					ti.setTitle(new String(map.get("title").toString()));
					ti.setDjs(map.get("djs").toString());		//����TopicItem����ĵ��������ֵ				
					ti.setRevert(map.get("revert").toString());	//����TopicItem����Ļظ�������ֵ
					ti.setFtsj(map.get("ftsj").toString().substring(0,19));	//���÷���ʱ������ֵ
					ti.setHtr(new String(map.get("htr").toString()));
					ti.setLastTime(map.get("htsj").toString().substring(0,19));
					ti.setGname(new String(map.get("gname").toString()));
					al.add(ti);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;														//���ؽ��
	}
	
	public List getApplyContent(String sql){
		List al = new ArrayList();							//��������List����
		System.out.println(sql);
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){		//�������
					Map map=(Map)rl.get(i);			//���List��ÿһ��Ϊһ��LinkedHashMap
					ApplyItem ai = new ApplyItem();	//����һ��ApplyItem����
					ai.setAid(map.get("AID").toString());
					ai.setUid(map.get("UID").toString());
					ai.setUname(new String(map.get("UName").toString()));
					ai.setTgid(map.get("TGID").toString());
					ai.setTgname(new String(map.get("TGName").toString()));
					ai.setReason(new String(map.get("AReason").toString()));
					ai.setFlag(map.get("AFlag").toString());
					ai.setStatus(new String(map.get("AStatus").toString()));
					al.add(ai);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();									//�����쳣����ӡ
		}
		return al;
	}
	public List getTopicDetail(String sql){
		List al = new ArrayList();							//��������List����		
		System.out.println(sql);
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){		//�������
					Map map=(Map)rl.get(i);			//���List��ÿһ��Ϊһ��LinkedHashMap
					TopicDetailItem tdi = new TopicDetailItem();	//����һ��TopicDetailItem����
					tdi.setUname(new String(map.get("uname").toString()));				
					tdi.setGender(new String(map.get("gender").toString()));
					tdi.setTx(new String(map.get("tx").toString()));
					tdi.setZcsj(map.get("zcsj").toString());		//����tdi�ķ�����ע��ʱ������ֵ
					tdi.setZhdl(new String( map.get("zhdl").toString()));//����tdi�ķ���������½ʱ������
					tdi.setTitle(new String(map.get("title").toString()));					
					tdi.setContent(new String(map.get("content").toString()));
					tdi.setTag(new String(map.get("tag").toString()));
					tdi.setFbsj(map.get("fbsj").toString().substring(0,19));
					al.add(tdi);					
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;														//���ؽ��		
	}
	
	public int getTotal(String sql,int span){						//�õ���ҳ��
		int total = 0;										//����ҳ������ֵ
		sql = "select count(*) from ("+sql+")a";
		int rows = jt.queryForInt(sql);					//ִ�в�ѯ�õ��ܼ�¼����			
		total = rows/span+((rows%span==0)?0:1);			//����õ���ҳ��						
	
		return total;
	}
	
	public User getUserInfo(String sql){
		User user = new User();							//��������User����		
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				Map map=(Map)rl.get(0);				//���List��ÿһ��Ϊһ��LinkedHashMap
				user.setUid(map.get("UID").toString());	//����User�����uid����
				user.setUname(new String(map.get("UName").toString()));
				user.setGender(new String(map.get("UGender").toString()));
				user.setEmail(map.get("UEmail").toString());//����User�����email����
				user.setRole(map.get("URole").toString());	//����User�����role����					
				user.setHead(map.get("UHead").toString());	//����User�����head����
				user.setRegDate(map.get("URegDate").toString());
				user.setLastLogin(map.get("ULastLogin").toString().substring(0,19));				
				user.setPermit(map.get("UPermit").toString());
				Object last = map.get("ULastEmit");
				if(last==null){
					user.setLastEmit("xxxx-xx-xx");				//û�з������κλظ�������
				}
				else{
					String lastEmit = last.toString();			//תΪ�ַ���
					user.setLastEmit(lastEmit.substring(0,19));	//�����������
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return user;
	}
	
	public List getStuList(String sql){
		List al = new ArrayList();							//��������List����			
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){		//�������
					Map map=(Map)rl.get(i);				//���List��ÿһ��Ϊһ��LinkedHashMap
					User user = new User();							//��������User����	
					user.setUid(map.get("UID").toString());	//����User�����uid����
					user.setUname(new String(map.get("UName").toString()));
					user.setTgname(new String(map.get("TGName").toString()));
					user.setGender(new String(map.get("UGender").toString()));
					user.setEmail(map.get("UEmail").toString());//����User�����email����	
					user.setTuid(map.get("TUID").toString());		
					user.setRegDate(map.get("URegDate").toString());
					user.setLastLogin(map.get("ULastLogin").toString().substring(0,19));				
					user.setPermit(map.get("UPermit").toString());				
					Object last = map.get("ULastEmit");
					if(last==null){
						user.setLastEmit("xxxx-xx-xx");				//û�з������κλظ�������
					}
					else{
						String lastEmit = last.toString();			//תΪ�ַ���
						user.setLastEmit(lastEmit.substring(0,19));	//�����������
					}
					al.add(user);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;
	}
	
	public List getUserList(String sql){
		List al = new ArrayList();							//��������List����			
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){		//�������
					Map map=(Map)rl.get(i);				//���List��ÿһ��Ϊһ��LinkedHashMap
					User user = new User();							//��������User����	
					user.setUid(map.get("UID").toString());	//����User�����uid����
					user.setUname(new String(map.get("UName").toString()));					
					user.setGender(new String(map.get("UGender").toString()));
					user.setEmail(map.get("UEmail").toString());	//����User�����email����	
					user.setRegDate(map.get("URegDate").toString());				
					user.setPermit(map.get("UPermit").toString());	
					user.setRole(map.get("URole").toString());				
					Object last = map.get("ULastEmit");
					if(last==null){
						user.setLastEmit("xxxx-xx-xx");				//û�з������κλظ�������
					}
					else{
						String lastEmit = last.toString();			//תΪ�ַ���
						user.setLastEmit(lastEmit.substring(0,19));	//�����������
					}
					last = map.get("ULastLogin");
					if(last==null){
						user.setLastLogin("xxxx-xx-xx");				//û�з������κλظ�������
					}
					else{
						String lastLogin = last.toString();				//תΪ�ַ���
						user.setLastLogin(lastLogin.substring(0,19));	//�����������
					}
					al.add(user);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;
	}
	
	public List getManageList(String sql){
		List al = new ArrayList();							//��������List����			
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){		//�������
					Map map=(Map)rl.get(i);				//���List��ÿһ��Ϊһ��LinkedHashMap
					User user = new User();							//��������User����	
					user.setUid(map.get("UID").toString());	//����User�����uid����
					user.setUname(new String(map.get("UName").toString()));				
					user.setRole(map.get("URole").toString());		
					al.add(user);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;
	}
	
	public List getCourseList(String sql){
		List al = new ArrayList();							//��������List����			
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){		//�������
					Map map=(Map)rl.get(i);				//���List��ÿһ��Ϊһ��LinkedHashMap
					CourseItem ci = new CourseItem();		//��������CourseItem����
					ci.setUid(map.get("UID").toString());	//����User�����uid����
					ci.setTgid(map.get("TGID").toString());
					ci.setTgname(new String(map.get("TGName").toString()));	
					ci.setTdetail(new String(map.get("TDetail").toString()));	
					ci.setUname(new String(map.get("UName").toString()));				
					al.add(ci);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;
	}
	
	public List getQuestionList(String sql){
		List al = new ArrayList();							//��������List����
		try{
			rl = jt.queryForList(sql);						//�����õ����
			if(rl.size()!=0){								//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){				//�������
					Map map=(Map)rl.get(i);					//���List��ÿһ��Ϊһ��LinkedHashMap
					TopicItem ti = new TopicItem();			//����һ��TopicItem����
					ti.setTid(map.get("TID").toString());	//����TopicItem�����������������ֵ
					ti.setFtr(new String(map.get("UName").toString()));
					ti.setTitle(new String(map.get("TTitle").toString()));
					ti.setFtsj(map.get("TDate").toString().substring(0,19));	//���÷���ʱ������ֵ
					ti.setGname(new String(map.get("TGName").toString()));
					al.add(ti);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;
	}
	
	public List getRevertList(String sql){
		List al = new ArrayList();							//��������List����
		try{
			rl = jt.queryForList(sql);						//�����õ����
			if(rl.size()!=0){								//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){				//�������
					Map map=(Map)rl.get(i);					//���List��ÿһ��Ϊһ��LinkedHashMap
					RevertItem ri = new RevertItem();		//����һ��RevertItem����
					ri.setRid(map.get("RID").toString());	//����RevertItem�����������������ֵ
					ri.setUname(new String(map.get("UName").toString()));
					String nr = map.get("RContent").toString();
					if(nr.length()>40){
						nr = nr.substring(0,40);
						nr = nr + "......";
					}
					ri.setNr(new String(nr));
					ri.setRdate(map.get("RDate").toString().substring(0,19));	//���÷���ʱ������ֵ
					al.add(ri);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return al;
	}
	
	public CourseItem getCourseInfo(String sql){
		CourseItem ci = new CourseItem();							//��������CourseItem����	
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){		//�������
					Map map=(Map)rl.get(i);				//���List��ÿһ��Ϊһ��LinkedHashMap
					ci.setUid(map.get("UID").toString());	//����User�����uid����
					ci.setTgid(map.get("TGID").toString());
					ci.setTgname(new String(map.get("TGName").toString()));	
					ci.setTdetail(new String(map.get("TDetail").toString()));	
					ci.setUname(new String(map.get("UName").toString()));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return ci;
	}
	
	public Map getCourse(String sql){
		Map map = new HashMap();							//��������Map����		
		System.out.println(sql);
		try{
			rl = jt.queryForList(sql);				//�����õ����
			if(rl.size()!=0){						//�����Ϊ�յ����
				for(int i=0;i<rl.size();i++){		//�������
					Map mp=(Map)rl.get(i);			//���List��ÿһ��Ϊһ��LinkedHashMap
					String tgid = 					//�õ��γ�ID
						mp.get("tgid").toString();	
					String gname = 					//�õ��γ�����
						new String(mp.get("gname").toString());
					map.put(tgid,gname);					
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();										//�����쳣����ӡ
		}
		return map;														//���ؽ��		
	}
	
	public String getStringInfo(String sql){
		String info = null;										//���������ַ�������
		try{
			info = (String)jt.queryForObject(sql,String.class);	//ִ�в�ѯ
		}
		catch(Exception e){
			info = null;										//���쳣������info��Ϊnull
		}
		return info;											//���ز�ѯ���
	}
	
	public boolean isExist(String sql){
		System.out.println("isExist "+sql);
		boolean result = false;
		rl = jt.queryForList(sql);							//ִ�в�ѯ
        if(rl.size()!=0){									//�жϲ�ѯ���
        	result = true;									//��������û���ֵ��־λΪtrue
        }
		return result;										//���ر�־λ
	}
	
	public boolean update(Vector<String> v){				//������
		boolean flag = true;													//���½����־
		dtm = new DataSourceTransactionManager(ds);		//�õ��������
		dtd = new DefaultTransactionDefinition();
        dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus ts = dtm.getTransaction(dtd);
        try{
        	for(int i=0;i<v.size();i++){
        		System.out.println("["+i+"]"+v.get(i));
        		sql = new String(v.get(i).getBytes());
        		jt.update(sql);								//ִ�и���
        	}
        	dtm.commit(ts);									//���쳣�������ύ����
       }
       catch(Exception e){
           dtm.rollback(ts);								//�����쳣�����лع�
           flag = false;									//���±�־����Ϊfalse
           e.printStackTrace();
       }
		return flag;
	}
	
	public boolean update(String sql){							//ִ�е���SQL���ķ���
		boolean flag = false;
		try{
			int result = jt.update(sql);		//ִ�и��µõ����¼�¼����
			if(result >= 0){						//���¼�¼��������һʱ
				flag = true;					//�����½����־��Ϊtrue
			}
		}
		catch(Exception e){					
			flag = false;					//����ʧ��
			e.printStackTrace();			
		}
		return flag;						//���ظ��½����־λ
	}
}