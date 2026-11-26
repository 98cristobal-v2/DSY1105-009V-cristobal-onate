$files = Get-ChildItem 'C:\Users\Cris\Desktop\*.pdf'
foreach ($file in $files) {
    Write-Output "=================================================="
    Write-Output "FILE: $($file.Name)"
    Write-Output "=================================================="
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    $text = [System.Text.Encoding]::ASCII.GetString($bytes)
    $matches = [System.Text.RegularExpressions.Regex]::Matches($text, '\(([^)]+)\)')
    foreach ($m in $matches) {
        $val = $m.Groups[1].Value
        if ($val.Length -gt 2 -and $val -notmatch '^[0-9\s\.\,\-]+$') {
            $val
        }
    }
}
