import _ from "lodash";
import React, { useCallback, useEffect, useState } from "react";
import { Container, Dropdown, Stack } from "react-bootstrap";
import "./History.css";
import EmptyListView from "../../../assets/EmptyListView.png";
import { useAxios } from "../../../context/AxiosContext";
import { ENDPOINTS } from "../../../utils/Constants";
import { AddFeedbackCard } from "../add_feedback/AddFeedbackCard";
import { HistoryContractCard } from "../history_contract_card/HistoryContractCard";

export const History = () => {
  const [selectedHistoryType, setSelectedHistoryType] = useState("Completed contracts");
  const [contracts, setContracts] = useState([]);
  const [filteredContracts, setFilteredContracts] = useState([]);
  const [searchText, setSearchText] = useState("");
  const [selectedContractId, setSelectedContractId] = useState(null);
  const { getRequest } = useAxios();

  useEffect(() => {
    loadContracts()
  }, []);

  useEffect(() => {
    filterContractsByType()
  }, [selectedHistoryType, contracts]);

  useEffect(() => {
    filterContractsBySearchText();
  }, [searchText]);

  useEffect(() => {
    if (filteredContracts.length > 0) {
      setSelectedContractId(filteredContracts[0].id);
    }
  }, [filteredContracts]);

  const loadContracts = async () => {
    try {
      const response = await getRequest(ENDPOINTS.GET_HISTORY_CONTRACTS, true);
      const contractsWithSelection = response.data.data.contracts.map(contract => ({
        ...contract,
        isSelected: false
      }));
      setContracts(contractsWithSelection);
    } catch (error) {
      console.log("Failed to load contracts", error);
    }
  }

  const filterContractsByType = () => {
    if (contracts.empty) return;
    const filtered = contracts.filter(contract => {
      switch (selectedHistoryType) {
        case "Completed contracts":
          return contract.historyType === "Completed";
        case "Requested contracts":
          return contract.historyType === "Requested";
        default:
          return true;
      }
    });
    setFilteredContracts(filtered);
  }

  const filterContractsBySearchText = () => {
    const filtered = contracts.filter(contract =>
      contract.serviceName.toLowerCase().includes(searchText.toLowerCase()) ||
      contract.serviceType.toLowerCase().includes(searchText.toLowerCase())
    ).filter(contract => {
        switch (selectedHistoryType) {
          case "Completed contracts":
            return contract.historyType === "Completed";
          case "Requested contracts":
            return contract.historyType === "Requested";
          default:
            return true;
        }
      }
    );
    setFilteredContracts(filtered);
  };


  const handleViewProfileClicked = (userId) => {
    // [TODO]: View profile of the user with user id as {userId}
  }

  const getSelectedContract = () => {
    return contracts.filter(contract => contract.id === selectedContractId)[0];
  }

  const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
    <Stack
      ref={ref}
      direction="horizontal"
      className="history-type-dropdown justify-content-between ps-4 pe-4 h-100"
      onClick={(e) => {
        e.preventDefault();
        onClick(e);
      }}
      gap={2}
    >
      <div>{children}</div>
      <div>&#x25bc;</div>
    </Stack>
  ));

  const handleHistoryTypeDropdownClick = (eventKey) => {
    switch (eventKey) {
      case "1":
        setSelectedHistoryType("Completed contracts");
        break;
      case "2":
        setSelectedHistoryType("Requested contracts");
        break;
      case "3":
        setSelectedHistoryType("All contracts");
        break;
      default:
        break;
    }
  };

  const debouncedSetSearchText = useCallback(
    _.debounce((value) => {
      setSearchText(value);
    }, 300), []
  );

  const handleSearchInputChange = (event) => {
    debouncedSetSearchText(event.target.value);
  };

  return (
    <Container className="history-contracts-container h-auto pt-4 pb-3 justify-content-start">
      <Stack direction="horizontal" className="search-box w-75 align-items-stretch">
        <input
          className="search-input w-75 pt-2 pb-2 ps-4 pe-4"
          placeholder="Search contract"
          onChange={handleSearchInputChange}
        />
        <div className="d-inline-flex w-25">
          <Dropdown onSelect={handleHistoryTypeDropdownClick} className="w-100">
            <Dropdown.Toggle as={CustomToggle} id="dropdown-custom-components">
              {selectedHistoryType}
            </Dropdown.Toggle>

            <Dropdown.Menu>
              <Dropdown.Item eventKey="1">Completed contracts</Dropdown.Item>
              <Dropdown.Item eventKey="2">Requested contracts</Dropdown.Item>
              <Dropdown.Item eventKey="3">All contracts</Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
        </div>
      </Stack>

      <Stack
        direction="horizontal"
        className="d-flex justify-content-between align-items-center pt-3 flex-md-row align-items-md-start flex-column"
        gap={5}
      >
        <div className="w-100">
          <Stack direction="vertical" className="contracts-list-view p-3">
            {
              filteredContracts.length > 0 ? (
                filteredContracts.map(contract => (
                  <HistoryContractCard
                    contract={contract}
                    isSelected={selectedContractId === contract.id}
                    onSelect={setSelectedContractId}
                    onViewProfileClicked={handleViewProfileClicked}
                    key={contract.id}
                  />
                ))
              ) : (
                <Container fluid className="empty-contracts-view d-flex align-items-center justify-content-center pb-5">
                  <div>
                    <Stack className="align-items-center" gap={3}>
                      <img src={EmptyListView} alt="NavigateLeft" width="200px" height="200px"/>
                      <div className="empty-contracts-text">No contracts available</div>
                    </Stack>
                  </div>
                </Container>
              )
            }
          </Stack>
        </div>

        <div>
          {
            filteredContracts.length > 0 && (
              selectedContractId ? (
                <Container fluid className="add-feedback-card d-flex align-items-center mt-2">
                  <AddFeedbackCard
                    contract={getSelectedContract()}
                    key={selectedContractId}
                  />
                </Container>
              ) : (
                <div className="select-feedback-title">
                  Select contract to give feedback
                </div>
              )
            )
          }
        </div>
      </Stack>
    </Container>
  );
}