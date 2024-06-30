<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="kr.happyjob.study.notice.dao.NoticeDao">

	<select id="noticelist" resultType="kr.happyjob.study.notice.model.Noticevo">
		/*kr.happyjob.study.notice.dao.NoticeDao.noticelist*/
		select an.notice_id
			  , ui.loginID 
		      , ui.name
		      , an.notice_tit
		      , DATE_FORMAT(an.regdate, '%y-%m-%d') as regdate
		      , an.hit
		from tb_adm_notice an 
		      join tb_userinfo ui on ui.loginID = an.loginID
	    <where>
	    
	    	<if test="(searchtitle != null) and (!searchtitle.equals(''))">
		    	and an.notice_tit Like CONCAT('%', #{searchtitle}, '%')
			</if>
	    	<if test="(searchstdate != null) and (!searchstdate.equals(''))">
		    	and an.regdate <![CDATA[ >= ]]> STR_TO_DATE(#{searchstdate}, '%Y-%m-%d')
			</if>
	    	<if test="(searcheddate != null) and (!searcheddate.equals(''))">
		    	and an.regdate <![CDATA[ <= ]]> STR_TO_DATE(#{searcheddate}, '%Y-%m-%d')
			</if>
			
	    </where>

		ORDER BY an.regdate DESC
		LIMIT #{startpos}, #{pagesize}

	</select><!-- end 목록 조회 -->
	
	<!-- 총 갯수 조회 -->
	<select id="noticecnt" resultType="int">
		/*kr.happyjob.study.notice.dao.NoticeDao.noticecnt*/
		select count(*)
		from tb_adm_notice an 
		      join tb_userinfo ui on ui.loginID = an.loginID
	    <where>
	    
	    	<if test="(searchtitle != null) and (!searchtitle.equals(''))">
		    	and an.notice_tit Like CONCAT('%', #{searchtitle}, '%')
			</if>
	    	<if test="(searchstdate != null) and (!searchstdate.equals(''))">
		    	and an.regdate <![CDATA[ >= ]]> STR_TO_DATE(#{searchstdate}, '%Y-%m-%d')
			</if>
	    	<if test="(searcheddate != null) and (!searcheddate.equals(''))">
		    	and an.regdate <![CDATA[ <= ]]> STR_TO_DATE(#{searcheddate}, '%Y-%m-%d')
			</if>
			
	    </where>
			
	</select>
	
	<!-- 파일 o 신규 공지 등록 -->
	<insert id="noticeSave">
		/*kr.happyjob.study.notice.dao.NoticeDao.noticeSave*/
		
	<!-- 	<selectKey resultType="int" keyProperty="notice_id" order="BEFORE">
			select IFNULL(max(notice_id),0)+1
			from tb_adm_notice
		</selectKey> -->		
				insert into tb_adm_notice 
					(	
						 loginID		
						, notice_tit	
						, notice_con	
						, regdate		
						, hit							
						,filename
						,physicalpath
						,logicalpath
						,filesize
						,fileext
						,file_yn
					) values (
						 #{loginID}
						, #{notice_tit}	
						, #{notice_con}	
						, now()		
						, 0
						<if	test="fileinfo.file_nm!=null">
						, #{fileinfo.file_nm} 	
						, #{fileinfo.file_loc}	
						, #{fileinfo.vrfile_loc} 	
						, #{fileinfo.file_size} 	
						, #{fileinfo.fileExtension} 
						, 'y'	
						</if>
						<if test="fileinfo.file_nm==null">
						, '' 	
						, ''	
						, '' 	
						, 0 	
						, ''
						, 'n'	
						</if>
					)
		</insert>
		
	
	<!-- 상세조회 -->
	<select id="noticeView" resultType="kr.happyjob.study.notice.model.Noticevo">
		/*kr.happyjob.study.notice.dao.NoticeDao.noticedtl*/
		SELECT an.notice_id
			  , ui.loginID 
		      , ui.name
		      , an.notice_tit
		      , an.notice_con
		      , DATE_FORMAT(an.regdate, '%y-%m-%d') as regdate
		      , an.hit
        	  , an.filename as filename
        	  , an.physicalpath as physicalpath
        	  , an.logicalpath as logicalpath
        	  , an.filesize as filesize
        	  , an.fileext as fileext
        	  , an.file_yn as file_yn
		FROM tb_adm_notice an 
		      join tb_userinfo ui on ui.loginID = an.loginID
	    WHERE an.notice_id = #{notice_id}
	</select><!-- end 상세조회-->
	
	<!-- 조회수 증가 -->
	<update id="noticehitupdate">
	    UPDATE tb_adm_notice
	    SET hit = hit+1
	    WHERE notice_id=#{notice_id};
	</update>	
	
	
	<!-- 게시글 수정 -->
	<update id="noticeModify">
		/*kr.happyjob.study.notice.dao.NoticeDao.noticeModify*/		
			UPDATE tb_adm_notice
			SET
				notice_tit =#{notice_tit}	
				,notice_con =#{notice_con}
				<if test="fileinfo.file_nm==null ">
				,filename = null
				,physicalpath = null
				,logicalpath = null
				,filesize = null
				,fileext = null
				,file_yn = 'n'
				</if>
				<if test="fileinfo.file_nm!=null ">
				,filename = #{fileinfo.file_nm}	
				,physicalpath = #{fileinfo.file_loc}	
				,logicalpath = #{fileinfo.vrfile_loc}	
				,filesize = #{fileinfo.file_size}	
				,fileext = #{fileinfo.fileExtension}	
				,file_yn = 'y'
				</if>
		    WHERE notice_id=#{noticeNo}; 	
		
	</update>
	
	<!-- 공지 삭제 -->
	<delete id="noticeDelete">
		DELETE
		FROM tb_adm_notice
		WHERE notice_id = #{noticeNo}
	</delete>
	
</mapper>