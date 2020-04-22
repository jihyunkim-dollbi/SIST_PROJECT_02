package com.sist.board.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.*;

public class ReplyBoardDAO {
	private static SqlSessionFactory ssf;
	static
	{
		
		ssf=CreateSqlSessionFactory.getSsf();
	}
	
	
	// [답글형 게시판 리스트] 
	public static List<BoardVO> replyListData(Map map)
	{
		List<BoardVO> list=new ArrayList<BoardVO>();
		SqlSession session=null;
		
		try
		{
			session=ssf.openSession();	
			list=session.selectList("replyListData",map); 
			
		}catch(Exception ex)
		{
			System.out.println("replyListData: "+ex.getMessage());
		}
		finally
		{
			if(session!=null)
				session.close();
		}
		
		return list;
	}
	
	// [답글형 게시판 총 페이지]
	public static int replyTotalPage()
	{
		int total=0;
		SqlSession session=null;
		
		try
		{
			session=ssf.openSession();	
			total=session.selectOne("replyTotalPage");
			
		}catch(Exception ex)
		{
			System.out.println("replyTotalPage: "+ex.getMessage());
		}
		finally
		{
			if(session!=null)
				session.close(); // 반환 
		}
		
		return total;
	}
	
	// [답글형 게시판 상세페이지] 
	public static BoardVO replyDetailData(int no)
	{
		BoardVO vo = new BoardVO();
		SqlSession session=null;
		
		try
		{
			session=ssf.openSession();	
			vo=session.selectOne("replyDetailData",no); 
			
		}catch(Exception ex)
		{
			System.out.println("replyDetailData: "+ex.getMessage());
		}
		finally
		{
			if(session!=null)
				session.close(); 
		}
		return vo;
	}
	
	// [상세피이지 조회 시 조회 수 증가]
	public static BoardVO hitIncrement(int no)
	{
		BoardVO vo = new BoardVO();
		SqlSession session=null;
		
		try
		{
			session=ssf.openSession();	
			session.update("hitIncrement",no);
			session.commit(); // ★★★
			
			vo=session.selectOne("replyDetailData", no); // ★★★
			
		}catch(Exception ex)
		{
			System.out.println("hitIncrement: "+ex.getMessage());
		}
		finally
		{
			if(session!=null)
				session.close(); 
		}
		
		return vo;
	}
	
	
	// [답글형 게시판 글쓰기] 
	public static BoardVO replyInsertData(BoardVO vo)
	{
		SqlSession session=null;
		
		try
		{
			session=ssf.openSession(true); // 오토커밋하게 true로 변경 	
			session.insert("replyInsertData",vo); // ★★★★
			
		}catch(Exception ex)
		{
			System.out.println("replyInsertData: "+ex.getMessage());
		}
		finally
		{
			if(session!=null)
				session.close(); 
		}
		return vo;
	}
	
	// [글 수정] - 비번 확인 
	public static String replyGetPassword(int no)
	{
		String pwd="";
		SqlSession session=null;
		
		try
		{
			session=ssf.openSession();	
			pwd=session.selectOne("replyGetPassword",no); 
			
		}catch(Exception ex)
		{
			System.out.println("replyGetPassword: "+ex.getMessage());
		}
		finally
		{
			if(session!=null)
				session.close(); 
		}
		return pwd;
	}
	
	
	// [글 수정] - 데이터 update 
	public static BoardVO replyUpdateData(BoardVO vo)
	{
		SqlSession session=null;
		
		try
		{
			session=ssf.openSession(true); 
			session.update("replyUpdateData",vo); 

		}catch(Exception ex)
		{
			System.out.println("replyUpdateData: "+ex.getMessage());
		}
		finally
		{
			if(session!=null)
				session.close(); 
		}
		return vo;
	}
	
	public static void replyReplyInsert(int pno,BoardVO vo){
		SqlSession session=null;
		try {
			session=ssf.openSession();
			BoardVO pvo=session.selectOne("replayParentInfoData",pno);
			session.update("replyGroupStepIncrement",pvo);
			//replyReplyInsert
			//replyDepthIncrement
			vo.setGroup_id(pvo.getGroup_id());
			vo.setGroup_step(pvo.getGroup_step()+1);
			vo.setGroup_tab(pvo.getGroup_tab()+1);
			vo.setRoot(pno);
			
			session.insert("replyReplyInsert", vo);
			session.update("replyDepthIncrement", pno);
			
			session.commit();	
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			session.rollback();					//실패하면 롤백
		}finally{
			if(session!=null)
				session.close();
		}
	}
	
	public static boolean replyDelete(int no,String pwd){
		boolean bCheck=false;
		SqlSession session=null;
		try {
			session=ssf.openSession();
			String db_pwd=session.selectOne("replyGetPassword", no);
			if(db_pwd.equals(pwd)){
				bCheck=true;
				BoardVO vo=session.selectOne("replyDeleteInfoData", no);
				if(vo.getDepth()==0){
					session.delete("replyDelete",no);
				}else{
					vo.setSubject("관리자가 삭제한 게시물입니다");
					vo.setContent("관리자가 삭제한 게시물입니다");
					vo.setNo(no);
					session.update("replySubjectUpdate",vo);
				}
				session.update("replyDepthDecrement", vo.getRoot());
			}else{
				bCheck=false;
			}
			session.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			session.rollback();
		}finally{
			if(session!=null)
				session.close();
		}
		return bCheck;
	}
	
	
}



