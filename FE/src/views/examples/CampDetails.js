import camper4 from 'assets/img/brand/camper3.png';
import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { addUserFavorite } from "../../api/addUserFavorites";
import { fetchCampDetails } from '../../api/fetchCampDetails';
import { getUserFavorites } from "../../api/getUserFavorites";
import { removeUserFavorite } from "../../api/removeUserFavorites";

const CampDetails = () => {
    const { id } = useParams();
    const [campDetails, setCampDetails] = useState(null);
    const [isFavorite, setIsFavorite] = useState(false);
    const imgRef = useRef(null);

    useEffect(() => {
        if (id) {
            fetchCampDetails(Number(id))  // ID를 숫자로 변환하여 API 호출
                .then(data => {
                    if (data) {
                        setCampDetails(data);  // 상태 업데이트
                    } else {
                        console.log("No data returned for this ID");
                    }
                })
                .catch(err => console.error('Failed to fetch camp details', err));
        }
    }, [id]);

    useEffect(() => {
        if (campDetails) {
            checkFavorite(campDetails.campDefaultInfoDto.contentId);
        }
    }, [campDetails]);

    const checkFavorite = async (campId) => {
        try {
            const favorites = await getUserFavorites();
            const isFav = favorites.some(fav => fav.contentId === campId);
            setIsFavorite(isFav);
        } catch (error) {
            console.error('Error fetching favorites', error);
        }
    };

    const handleFavoriteClick = async (contentId) => {
        if (isFavorite) {
            if (window.confirm('즐겨찾기에서 삭제하시겠습니까?')) {
                try {
                    const result = await removeUserFavorite(contentId);
                    console.log(result);
                    if (result && result.message === "Favorite removed successfully") {
                        alert("성공적으로 즐겨찾기를 삭제했습니다");
                        setIsFavorite(false);
                    } else {
                        alert('Failed to remove favorite.');
                    }
                } catch (error) {
                    console.error('Failed to remove favorite', error);
                }
            }
        } else {
            if (window.confirm('즐겨찾기에 추가하시겠습니까?')) {
                try {
                    console.log("contentId" + contentId);
                    const result = await addUserFavorite(contentId);
                    console.log(result);
                    if (result && result.contentId) {
                        alert("성공적으로 즐겨찾기를 추가했습니다.");
                        setIsFavorite(true);
                    } else {
                        alert('Failed to add favorite.');
                    }
                } catch (error) {
                    console.error('Failed to add favorite', error);
                }
            }
        }
    };

    if (!campDetails) return <div>Loading...</div>;

    const handleError = (e) => {
        e.target.onerror = null; 
        e.target.src = camper4;
        imgRef.current.style.width = '500px';
        imgRef.current.style.height = '300px';
        imgRef.current.style.objectFit = 'cover';
        imgRef.current.style.backgroundColor = '#ffffff';
      };

    return (
        <div className="camp-details" style={{ margin: '5%' }}>
            <div className="modal-header">
            </div>
            <div className="modal-body">
                <div className="modal-body-content" style={{ display: 'flex', flexDirection: 'row', alignItems: 'flex-start', gap: "10px"}}>
                    <div style={{ display: 'flex', flexDirection: 'column', flex: 1 }}>
                        <div style={{ marginBottom: "20px", flex: 1 }}>
                            <div style={{display: 'flex', alignItems: 'center', justifyContent: 'flex-start' , gap: "10px"}}>
                                <div>
                                    <h2 className="fontNB">{campDetails.campDefaultInfoDto.facltNm}</h2>
                                </div>
                                <div>
                                    <svg xmlns="http://www.w3.org/2000/svg" x="0px" y="0px" width="50" height="50" viewBox="0 0 24 24" fill={isFavorite ? 'black' : 'none'} stroke="black" strokeWidth="2"
                                        onClick={() => handleFavoriteClick(campDetails.campDefaultInfoDto.contentId)}>
                                        <path d="M 6 2 C 4.8444444 2 4 2.9666667 4 4 L 4 22.039062 L 12 19.066406 L 20 22.039062 L 20 20.599609 L 20 4 C 20 3.4777778 19.808671 2.9453899 19.431641 2.5683594 C 19.05461 2.1913289 18.522222 2 18 2 L 6 2 z"></path>
                                    </svg>
                                </div>
                            </div>
                            {campDetails.campDefaultInfoDto.starAvg && (
                                <p><img width="30" height="30" src="https://img.icons8.com/emoji/48/star-emoji.png" alt="star-emoji" />
                                    <strong>  Campers 평점:</strong> {campDetails.campDefaultInfoDto.starAvg}</p>
                            )}
                        </div>
                        <div style={{marginRight: "10px", flex: 5}}>
                        <img src={campDetails.campDefaultInfoDto.firstImageUrl || camper4} 
                        objectFit="cover"
                        alt={campDetails.campDefaultInfoDto.facltNm}
                        ref={imgRef}
                        style={{ borderRadius: '10px', height:"480px",width:"720px"}} 
                        onError={handleError}  />
                        
                        </div>
                    </div>
                    <div style={{ flex: 1 }}>
                        <h4 className="fontNeo">캠핑장 기본 정보
                        <img width="40" height="40" src="https://img.icons8.com/emoji/48/camping.png" alt="camping"/>
                        </h4>
                        <div className="details">
                            {campDetails.campDefaultInfoDto.addr1 && (
                                <p><strong>주소:</strong> {campDetails.campDefaultInfoDto.addr1}</p>
                            )}
                            {campDetails.campDefaultInfoDto.addr2 && (
                                <p><strong>주소상세:</strong> {campDetails.campDefaultInfoDto.addr2}</p>
                            )}
                            {campDetails.campDefaultInfoDto.direction && (
                                <p><strong>오시는 길:</strong> {campDetails.campDefaultInfoDto.direction}</p>
                            )}
                            {campDetails.campDefaultInfoDto.tel && (
                                <p><strong>전화번호:</strong> {campDetails.campDefaultInfoDto.tel}</p>
                            )}
                            {campDetails.campDefaultInfoDto.homepage && (
                                <p><strong>홈페이지:</strong> <a href={campDetails.campDefaultInfoDto.homepage} target="_blank" rel="noopener noreferrer">{campDetails.campDefaultInfoDto.homepage}</a></p>
                            )}
                            {campDetails.campDefaultInfoDto.resveUrl && (
                                <p><strong>예약 링크:</strong> <a href={campDetails.campDefaultInfoDto.resveUrl} target="_blank" rel="noopener noreferrer">{campDetails.campDefaultInfoDto.resveUrl}</a></p>
                            )}
                            {campDetails.campDefaultInfoDto.openPdcl && (
                                <p><strong>개장 기간:</strong> {campDetails.campDefaultInfoDto.openPdcl}</p>
                            )}
                            {campDetails.campDefaultInfoDto.lineIntro && (
                                <p><strong>한줄 소개:</strong> {campDetails.campDefaultInfoDto.lineIntro}</p>
                            )}
                            {campDetails.campDefaultInfoDto.intro && (
                                <p><strong>소개:</strong> {campDetails.campDefaultInfoDto.intro}</p>
                            )}
                            {campDetails.campDefaultInfoDto.induty && (
                                <p><strong>업종:</strong> {campDetails.campDefaultInfoDto.induty}</p>
                            )}
                            {campDetails.campDefaultInfoDto.lctCl && (
                                <p><strong>입지구분:</strong> {campDetails.campDefaultInfoDto.lctCl}</p>
                            )}
                            {campDetails.campDefaultInfoDto.operPdcl && (
                                <p><strong>운영 기간:</strong> {campDetails.campDefaultInfoDto.operPdcl}</p>
                            )}
                            {campDetails.campDefaultInfoDto.operDeCl && (
                                <p><strong>운영일:</strong> {campDetails.campDefaultInfoDto.operDeCl}</p>
                            )}
                        </div>
                        <h4 className="fontNeo" style={{ marginTop: '20px' }}>캠핑장 부가 정보
                        <img width="40" height="40" src="https://img.icons8.com/emoji/48/camping.png" alt="camping"/>
                        </h4>
                        <div className="details">
                            {campDetails.campEtcInfoDto.exprnProgrmAt === 'Y' ? (
                                <p>체험 프로그램 있음</p>
                            ) : (
                                campDetails.campEtcInfoDto.exprnProgrmAt === 'N' && (
                                    <p>체험 프로그램 없음</p>
                                )
                            )}
                            {campDetails.campEtcInfoDto.extshrCo && (
                                <p><strong>화로 개수:</strong> {campDetails.campEtcInfoDto.extshrCo}</p>
                            )}
                            {campDetails.campEtcInfoDto.frprvtWrppCo && (
                                <p><strong>개인화장실 개수:</strong> {campDetails.campEtcInfoDto.frprvtWrppCo}</p>
                            )}
                            {campDetails.campEtcInfoDto.frprvtSandCo && (
                                <p><strong>개인 샤워실 개수:</strong> {campDetails.campEtcInfoDto.frprvtSandCo}</p>
                            )}
                            {campDetails.campEtcInfoDto.swrmCo && (
                                <p><strong>공용 샤워실 개수:</strong> {campDetails.campEtcInfoDto.swrmCo}</p>
                            )}
                            <div style={{ display: 'flex', flexDirection: 'column' }}>
                                {campDetails.campEtcInfoDto.autoSiteCo != 0 && (
                                    <p><strong>자동차 </strong> {campDetails.campEtcInfoDto.autoSiteCo}개 </p>
                                )}
                                {campDetails.campEtcInfoDto.glampSiteCo != 0 && (
                                    <p><strong>글램핑 </strong> {campDetails.campEtcInfoDto.glampSiteCo}개 </p>
                                )}
                                {campDetails.campEtcInfoDto.caravSiteCo != 0 && (
                                    <p><strong>카라반 </strong> {campDetails.campEtcInfoDto.caravSiteCo}개 </p>
                                )}
                            </div>
                            {campDetails.campEtcInfoDto.Tooltip && (
                                <p><strong>툴팁 </strong> {campDetails.campEtcInfoDto.Tooltip}</p>
                            )}
                            {campDetails.campEtcInfoDto.glampInnerFclty && (
                                <p><strong>글램핑 내부시설:</strong> {campDetails.campEtcInfoDto.glampInnerFclty}</p>
                            )}
                            {campDetails.campEtcInfoDto.caravInnerFclty && (
                                <p><strong>카라반 내부시설:</strong> {campDetails.campEtcInfoDto.caravInnerFclty}</p>
                            )}
                            {campDetails.campEtcInfoDto.trierAcmpnyAt === 'Y' ? (
                                <p>개인 트레일러 동반 가능</p>
                            ) : (
                                campDetails.campEtcInfoDto.trierAcmpnyAt === 'N' && (
                                    <p>개인 트레일러 동반 불가</p>
                                )
                            )}
                            {campDetails.campEtcInfoDto.caravAcmpnyAt === 'Y' ? (
                                <p>개인 카라반 동반 가능</p>
                            ) : (
                                campDetails.campEtcInfoDto.caravAcmpnyAt === 'N' && (
                                    <p>개인 카라반 동반 불가</p>
                                )
                            )}
                            {campDetails.campEtcInfoDto.sbrsCl && (
                                <p><strong>부대시설:</strong> {campDetails.campEtcInfoDto.sbrsCl}</p>
                            )}
                            {campDetails.campEtcInfoDto.sbrsEtc && (
                                <p><strong>부대시설 기타:</strong> {campDetails.campEtcInfoDto.sbrsEtc}</p>
                            )}
                            {campDetails.campEtcInfoDto.posblFcltyCl && (
                                <p><strong>주변이용가능시설:</strong> {campDetails.campEtcInfoDto.posblFcltyCl}</p>
                            )}
                            {campDetails.campEtcInfoDto.posblFcltyEtc && (
                                <p><strong>주변이용가능시설 기타:</strong> {campDetails.campEtcInfoDto.posblFcltyEtc}</p>
                            )}
                            {campDetails.campEtcInfoDto.clturEventAt === 'Y' && campDetails.campEtcInfoDto.clturEvent && (
                                <p><strong>자체문화행사:</strong> {campDetails.campEtcInfoDto.clturEvent}</p>
                            )}
                            {campDetails.campEtcInfoDto.exprnProgrmAt === 'Y' && campDetails.campEtcInfoDto.exprnProgrm && (
                                <p><strong>자체문화행사:</strong> {campDetails.campEtcInfoDto.exprnProgrm}</p>
                            )}
                            {campDetails.campEtcInfoDto.themaEnvrnCl && (
                                <p><strong>테마환경:</strong> {campDetails.campEtcInfoDto.themaEnvrnCl}</p>
                            )}
                            {campDetails.campEtcInfoDto.eqpmnLendCl && (
                                <p><strong>캠핑장비대여:</strong> {campDetails.campEtcInfoDto.eqpmnLendCl}</p>
                            )}
                            {campDetails.campEtcInfoDto.animalCmgCl === 'Y' ? (
                                <p>애완동물 출입 가능</p>
                            ) : (
                                campDetails.campEtcInfoDto.animalCmgCl === 'N' && (
                                    <p>애완동물 출입 불가</p>
                                )
                            )}
                        </div>
                    </div>
                </div>
            </div>
            <div className="modal-footer">
            </div>
        </div>
    );
}

export default CampDetails;
