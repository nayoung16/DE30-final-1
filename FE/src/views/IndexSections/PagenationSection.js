import { useState } from "react";
import { Pagination, PaginationItem, PaginationLink } from "reactstrap";

const PaginationSection = ({ currentPage, totalPages, paginate }) => {
  const [currentGroup, setCurrentGroup] = useState(0);
  const pageNumbers = [];

  for (let i = 1; i <= totalPages; i++) {
    pageNumbers.push(i);
  }

  const pagesPerGroup = 5;
  const totalGroups = Math.ceil(totalPages / pagesPerGroup);
  const startIndex = currentGroup * pagesPerGroup;
  const endIndex = Math.min(startIndex + pagesPerGroup, totalPages);

  const handlePreviousGroup = (e) => {
    e.preventDefault();
    if (currentGroup > 0) {
      setCurrentGroup(currentGroup - 1);
    }
  };

  const handleNextGroup = (e) => {
    e.preventDefault();
    if (currentGroup < totalGroups - 1) {
      setCurrentGroup(currentGroup + 1);
    }
  };

  return (
    <nav aria-label="Page navigation example" style={{ display: 'flex', justifyContent: 'center' }}>
      <Pagination>
        <PaginationItem disabled={currentGroup === 0}>
          <PaginationLink
            previous
            href="#pablo"
            onClick={handlePreviousGroup}
          >
            <i className="fa fa-angle-left" />
          </PaginationLink>
        </PaginationItem>
        {pageNumbers.slice(startIndex, endIndex).map(number => (
          <PaginationItem key={number} className={number === currentPage ? 'active' : ''}>
            <PaginationLink
              href="#pablo"
              onClick={(e) => { e.preventDefault(); paginate(number); }}
            >
              {number}
            </PaginationLink>
          </PaginationItem>
        ))}
        <PaginationItem disabled={currentGroup === totalGroups - 1}>
          <PaginationLink
            next
            href="#pablo"
            onClick={handleNextGroup}
          >
            <i className="fa fa-angle-right" />
          </PaginationLink>
        </PaginationItem>
      </Pagination>
    </nav>
  );
};

export default PaginationSection;
