import jsPDF from 'jspdf';
import 'jspdf-autotable';
import { APP_LOGO_BASE64_STRING } from "./Constants";

export const CreateAndOpenContractDocument = (contract) => {
  const doc = new jsPDF();
  const details = [
    ['Contract Status', contract.status],
    ['Service Requester', contract.userName],
    ['Service Cost', `${contract.perHourRate}$/hr`],
    ['Service Type', `${contract.serviceType}`],
    ['Service Provider', contract.serviceProviderName]
  ];

  doc.addImage(APP_LOGO_BASE64_STRING, 'PNG', 15, 15, 30, 20);

  doc.setFont('undefined', 'bold')
    .setFontSize(16)
    .text('Contract details', 15, 50);

  doc.setFont('undefined', 'bold')
    .setFontSize(14)
    .text("Service name", 15, 60);
  doc.setFont('undefined', 'normal')
    .setFontSize(12)
    .text(contract.serviceName, 15, 67);

  doc.setFont('undefined', 'bold')
    .setFontSize(14)
    .text("Service description", 15, 77);
  doc.setFont('undefined', 'normal')
    .setFontSize(12)
    .text(contract.serviceDescription, 15, 84);

  doc.setFont('undefined', 'bold')
    .setFontSize(14)
    .text("More information", 15, 94);

  doc.setFontSize(14);
  doc.autoTable({
    startY: 99,
    head: [['Type', 'Value']],
    body: details,
    theme: 'grid',
    headStyles: { fillColor: [239, 83, 102] },
    alternateRowStyles: { fillColor: [245, 245, 245] },
    margin: { top: 99 }
  });

  doc.setFontSize(10);
  const pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
  doc.setFont('undefined', 'normal')
    .text(`E-Signed by ${contract.serviceProviderName}`, 15, pageHeight  - 10);

  const pdfBlob = doc.output('blob');
  const pdfUrl = URL.createObjectURL(pdfBlob);
  window.open(pdfUrl, '_blank');
}
