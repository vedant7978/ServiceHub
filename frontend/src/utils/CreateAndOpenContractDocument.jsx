import jsPDF from 'jspdf';
import 'jspdf-autotable';
import { APP_LOGO_BASE64_STRING } from "./Constants";

export const CreateAndOpenContractDocument = (contract) => {
  const maxLength = 500;
  const xPosition = 15;
  const doc = new jsPDF({
    orientation: "p",
    unit: "px"
  });
  const details = [
    ['Contract Status', contract.status],
    ['Service Requester', contract.serviceRequesterName],
    ['Service Cost', `${contract.perHourRate}$/hr`],
    ['Service Type', `${contract.serviceType}`],
    ['Service Provider', contract.serviceProviderName]
  ];

  doc.addImage(APP_LOGO_BASE64_STRING, 'PNG', 15, 15, 30, 20);

  let text = "Contract details";
  let yPosition = 55;
  let lineHeight = doc.getLineHeight(text) / doc.internal.scaleFactor;
  let splittedText = doc.splitTextToSize(text, maxLength);
  let lines = splittedText.length;
  let blockHeight = lines * lineHeight;
  doc.setFont('undefined', 'bold')
    .setFontSize(16)
    .text(splittedText, xPosition, yPosition);

  text = "Service name"
  yPosition += blockHeight + 5;
  lineHeight = doc.getLineHeight(text) / doc.internal.scaleFactor;
  splittedText = doc.splitTextToSize(text, maxLength);
  lines = splittedText.length;
  blockHeight = lines * lineHeight;
  doc.setFont('undefined', 'bold')
    .setFontSize(14)
    .text(splittedText, xPosition, yPosition);

  text = contract.serviceName
  yPosition += blockHeight;
  lineHeight = doc.getLineHeight(text) / doc.internal.scaleFactor;
  splittedText = doc.splitTextToSize(text, maxLength);
  lines = splittedText.length;
  blockHeight = lines * lineHeight;
  doc.setFont('undefined', 'normal')
    .setFontSize(12)
    .text(splittedText, xPosition, yPosition);

  text = "Service description"
  yPosition += blockHeight + 5;
  lineHeight = doc.getLineHeight(text) / doc.internal.scaleFactor;
  splittedText = doc.splitTextToSize(text, maxLength);
  lines = splittedText.length;
  blockHeight = lines * lineHeight;
  doc.setFont('undefined', 'bold')
    .setFontSize(14)
    .text(splittedText, xPosition, yPosition);

  text = contract.serviceDescription;
  yPosition += blockHeight + 5
  lineHeight = doc.getLineHeight(text) / doc.internal.scaleFactor;
  splittedText = doc.splitTextToSize(text, maxLength);
  lines = splittedText.length;
  blockHeight = lines * lineHeight;
  doc.setFont('undefined', 'normal')
    .setFontSize(12)
    .text(splittedText, xPosition, yPosition);

  text = "More information";
  yPosition += blockHeight + 5
  lineHeight = doc.getLineHeight(text) / doc.internal.scaleFactor;
  splittedText = doc.splitTextToSize(text, maxLength);
  lines = splittedText.length;
  blockHeight = lines * lineHeight;
  doc.setFont('undefined', 'bold')
    .setFontSize(14)
    .text(splittedText, xPosition, yPosition);

  yPosition += blockHeight
  doc.setFontSize(14);
  doc.autoTable({
    startY: yPosition,
    head: [['Type', 'Value']],
    body: details,
    theme: 'grid',
    headStyles: { fillColor: [239, 83, 102] },
    alternateRowStyles: { fillColor: [245, 245, 245] },
    margin: { top: yPosition }
  });

  doc.setFontSize(10);
  const pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
  doc.setFont('undefined', 'normal')
    .text(`E-Signed by ${contract.serviceProviderName}`, xPosition, pageHeight  - 10);

  const pdfBlob = doc.output('blob');
  const pdfUrl = URL.createObjectURL(pdfBlob);
  window.open(pdfUrl, '_blank');
}
