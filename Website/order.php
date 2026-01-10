<?php
// Simple backend: save order to a CSV file and redirect to WhatsApp for confirmation.
// Note: This is a minimal example. For production, validate inputs and secure file paths.

if($_SERVER['REQUEST_METHOD'] === 'POST'){
    $name = trim($_POST['name'] ?? '');
    $phone = trim($_POST['phone'] ?? '');
    $details = trim($_POST['details'] ?? '');
    $time = date('Y-m-d H:i:s');

    if($name === '' || $phone === '' || $details === ''){
        header('Location: index.html#order');
        exit;
    }

    $line = [$time, str_replace(["\r","\n"],[' ',' '],$name), $phone, str_replace(["\r","\n"],[' ',' '],$details)];
    $fp = fopen('orders.csv','a');
    if($fp){
        fputcsv($fp, $line);
        fclose($fp);
    }

    // Redirect user to WhatsApp with prefilled message
    $msg = rawurlencode("Hi Bagia's Frozen Foods,\nI would like to place an order:\nName: $name\nPhone: $phone\nOrder: $details\n");
    $wa = "https://wa.me/918780448636?text={$msg}";
    header('Location: ' . $wa);
    exit;
} else {
    header('Location: index.html');
    exit;
}
?>