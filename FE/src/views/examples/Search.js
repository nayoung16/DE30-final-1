import { fetchCampNames } from "api/getCampNames";
import { searchCamp } from "api/searchCamp";
import "assets/css/argon-design-system-react.css";
import camper4 from 'assets/img/brand/camper3.png';
import DemoNavbar from "components/Navbars/DemoNavbar.js";
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from "react-router-dom";
import { Input, InputGroup } from "reactstrap";
import PaginationSection from 'views/IndexSections/PagenationSection';
import '../../assets/css/campDetails.css';
import '../../assets/css/fonts.css';

export default function Search({ isLogin, setIsLogin }) {
  const navigate = useNavigate();
  const mainRef = useRef(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [resultsPerPage] = useState(6);
  const [autocompleteResults, setAutocompleteResults] = useState([]);
  const [campNames, setCampNames] = useState([]);

  useEffect(() => {
    if (!isLogin) {
      navigate('/login-page');
    }
  }, [isLogin, setIsLogin, navigate]);

  const handleDetailsClick = (contentId) => {
    navigate(`/camp-details/${contentId}`);
  };

  const handleSearch = async () => {
    try {
      const results = await searchCamp(searchQuery);
      setSearchResults(results || []);
      setAutocompleteResults([]); // 검색 버튼을 클릭했을 때 자동완성 목록을 비웁니다.
      setCurrentPage(1); // 검색 결과 업데이트 시 첫 페이지로 설정
    } catch (error) {
      console.error('Error during search', error);
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleSearch();
    }
  };

  const handleSearchClick = () => {
    handleSearch();
  };

  const handleAutocompleteClick = (item) => {
    setSearchQuery(item);
    setAutocompleteResults([]);
  };

  // 자동완성 데이터를 검색하는 함수
  const fetchAutocompleteResults = (query) => {
    if (!query || !campNames) {
      setAutocompleteResults([]);
      return;
    }
    const lowerCaseQuery = query.toLowerCase();
    const filteredResults = campNames.filter(item =>
        item && item.toLowerCase().includes(lowerCaseQuery)
    );
    setAutocompleteResults(filteredResults);
  };

  // 검색어 변경 시 자동완성 결과 업데이트
  const handleSearchChange = (e) => {
    const value = e.target.value;
    setSearchQuery(value);
    fetchAutocompleteResults(value);
  };

  useEffect(() => {
    fetchCampNames().then(data => {
        console.log("Fetched data:", data); // Check what's being fetched
        setCampNames(data);
    }).catch(error => {
        console.error('Error loading camp names:', error);
    });
  }, []);

  // 페이지네이션 관련 함수
  const indexOfLastResult = currentPage * resultsPerPage;
  const indexOfFirstResult = indexOfLastResult - resultsPerPage;
  const currentResults = searchResults.slice(indexOfFirstResult, indexOfLastResult);
  const totalPages = Math.ceil(searchResults.length / resultsPerPage);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <>
      <DemoNavbar isLogin={isLogin} setIsLogin={setIsLogin} />
      <main className="search-page" ref={mainRef}>
        <section className="section-profile-custom section-shaped my-0">
          <div className="shape shape-style-1 shape-custom">
            <span />
            <span />
            <span />
            <span />
            <span />
            <span />
            <span />
          </div>
        </section>
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '60vh' }}>
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '20vh' }}>
            <h1 className="fontNB" style={{ color: 'black', fontSize: '40px'}}>새로운 캠핑 스팟을 찾아보세요
            <img width="48" height="48" src="https://img.icons8.com/emoji/48/camping.png" alt="camping"/>
            </h1>
            <p className="lead">Search for your new Camping Spot !!</p>
          </div>
          <div style={{ width: '80%', display: 'flex', justifyContent: 'center', paddingRight :'10%', paddingLeft: '10%', paddingTop: '3%' }}>
              <InputGroup style={{ width: '80%' }}>
                <Input
                  placeholder="Search"
                  type="text"
                  value={searchQuery}
                  onChange={handleSearchChange}
                  onKeyPress={handleKeyPress}
                  onBlur={() => setAutocompleteResults([])} // 입력 상자 외부를 클릭했을 때 자동완성 목록을 비웁니다.
                />
                <button type="button" className="btn-1 ml-1 btn btn-success" onClick={handleSearchClick}>Search</button>
                {autocompleteResults.length > 0 && (
                  <ul style={{ 
                    listStyleType: 'none', 
                    padding: 0,
                    width: '100%', // Ensure the dropdown matches the input's width
                    position: 'absolute', // Position it below the input box
                    top: '100%', // Position the dropdown right below the input
                    backgroundColor: 'white', // Background color for visibility
                    boxShadow: '0px 4px 6px rgba(0, 0, 0, 0.1)', // Optional: adds shadow for depth
                    zIndex: 1000 // Ensures the dropdown is on top of other content
                  }}>
                    {autocompleteResults.map((item, index) => (
                      <li key={index} onMouseDown={() => handleAutocompleteClick(item)} style={{
                        padding: '10px 20px', // Padding for each item
                        cursor: 'pointer', // Cursor indicates clickable
                        borderBottom: '1px solid #ccc' // Divider between items
                      }}>
                        {item}
                      </li>
                    ))}
                  </ul>
                )}
              </InputGroup>
          </div>
        </div>
        <div className="mt-4" style={{ display: 'flex' }}>
          <div style={{ display: 'flex', flex: 1, flexWrap: 'wrap', backgroundColor: 'white', padding: '10px', borderRadius: '5px', marginLeft: '15%' }}>
            {Array.isArray(currentResults) && currentResults.map((result) => (
              <div className="shadow card" key={result.contentId} style={{ width: '40%', margin: '1%', marginBottom: '20px', borderRadius: '10px', padding: '15px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <img 
                  src={result.firstImageUrl || camper4} 
                  alt="캠핑장 사진" 
                  style={{ 
                    width: '500px', 
                    height: '300px', 
                    objectFit: 'cover', 
                    backgroundColor: '#ffffff', 
                    borderRadius: '5px', 
                    padding: '5px', 
                    marginBottom: '10px' 
                  }}
                  onError={(e) => { e.target.onerror = null; e.target.src = camper4; }} 
                />
                <h4 className="mb-0 mx-auto fontNeo">{result.facltNm}</h4>
                <p className="mb-1 mx-auto fontNL">{result.addr1}</p> {/* 캠핑장 위치 */}
                <button type="button" className="btn-1 btn-neutral mx-auto btn btn-default" 
                  style={{ marginTop: "5px" }}
                  onClick={() => handleDetailsClick(result.contentId)}>
                  상세정보 보기
                </button>
              </div>
            ))}
          </div>
        </div>
        <section>
          {searchResults.length > resultsPerPage && (
            <div style={{ display: 'flex', justifyContent: 'center' }}>
              <PaginationSection
                currentPage={currentPage}
                totalPages={totalPages}
                paginate={paginate}
              />
            </div>
          )}
        </section>
      </main>
    </>
  );
}
