package hi.duranno.web.archive;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Pagination {

    //1페이지당 10개의 게시글
    private int pageSize = 10;
    //5개의 블럭
    private int blockSize = 5;
    //현재 페이지
    private int page = 1;
    //현재 블럭
    private int block = 1;
    //총 게시글 수
    private int totalListCnt;
    //총 페이지 수
    private int totalPageCnt;
    //총 블럭 수
    private int totalBlockCnt;
    //블럭 시작 페이지
    private int startPage = 1;
    //블럭 마지막 페이지
    private int endPage = 1;
    //DB index
    private int startIndex = 0;
    //이전 블럭의 마지막 페이지
    private int prevBlock;
    //다음 블럭의 시작 페이지
    private int nextBlock;

    public Pagination(int totalListCnt, int page) {

        /** 3. 현재 페이지 **/
        setPage(page);
        /** 5. 총 게시글 수 **/
        setTotalListCnt(totalListCnt);
        /** 6. 총 페이지 수 **/
        setTotalPageCnt((int) Math.ceil(totalListCnt * 1.0 / pageSize));
        /** 7. 총 블럭 수 **/
        setTotalBlockCnt((int) Math.ceil(totalPageCnt * 1.0 / blockSize));
        /** 4. 현재 블럭 **/
        setBlock((int) Math.ceil(page * 1.0 / blockSize));
        /** 8. 블럭 시작 페이지 **/
        setStartPage(((block - 1) * blockSize + 1));
        /** 9. 블럭 마지막 페이지 **/
        setEndPage(startPage + blockSize - 1);
        /** === 블럭 마지막 페이지에 대한 validation ===**/
        if(endPage > totalPageCnt){
            if (totalListCnt != 0) {
                this.endPage = totalPageCnt;
            } else {
                this.endPage = 1;
            }
        }

        /** 11. 이전 블럭(클릭 시, 이전 블럭 마지막 페이지) **/
        setPrevBlock(page - 1);

        /* === 이전 블럭에 대한 validation === */
        if(prevBlock < 1) {this.prevBlock = 1;}

        /** 12. 다음 블럭(클릭 시, 다음 블럭 첫번째 페이지) **/
        setNextBlock(page + 1);

        /* === 다음 블럭에 대한 validation ===*/
        if(nextBlock > totalPageCnt) {
            if (totalPageCnt != 0) {
                this.nextBlock = totalPageCnt;
            } else {
                this.nextBlock = 1;
            }
        }

        /** 10. DB 접근 시작 index **/
        setStartIndex((page-1) * pageSize);
    }
}
