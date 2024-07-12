import React, { useState } from 'react';
import { Container, Stack } from "react-bootstrap";
import "./ManageContracts.css";
import { History } from "../../components/manage_contracts/history/History";
import { PendingContracts } from "../../components/manage_contracts/pending_contracts/PendingContracts";

export default function ManageContracts() {
  const [selectedTab, setSelectedTab] = useState("PendingContracts");

  return (
    <Container fluid className="manage-contracts px-0 align-items-center">
      <Stack className="sub-nav-bar" direction="vertical">
        <Container>
          <div className="pending-contracts-title">Pending Contracts</div>
          <div className="pending-contracts-sub-title">
            All the contracts that are pending to be approved or rejected.
          </div>
          <Stack className="mt-3 pb-3" direction="horizontal" gap={3}>
            <div
              className={`tab-item pt-2 pb-2 ${selectedTab === 'PendingContracts' ? 'custom-underline' : ''}`}
              style={{
                color: selectedTab === 'PendingContracts' ? '#FFFFFF' : '#FFFFFF7F',
                cursor: 'pointer'
              }}
              onClick={() => setSelectedTab("PendingContracts")}
            >
              Pending Contracts
            </div>
            <div
              className={`tab-item pt-2 pb-2 ${selectedTab === 'History' ? 'custom-underline' : ''}`}
              style={{
                color: selectedTab === 'History' ? '#FFFFFF' : '#FFFFFF7F',
                cursor: 'pointer'
              }}
              onClick={() => setSelectedTab("History")}
            >
              History
            </div>
          </Stack>
        </Container>
      </Stack>
      {selectedTab === 'PendingContracts' && (
        <PendingContracts/>
      )}
      {selectedTab === 'History' && (
        <History/>
      )}
    </Container>
  );
};
