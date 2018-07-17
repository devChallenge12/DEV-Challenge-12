import UIKit

protocol EffectListViewControllerDelegate: class {
    func didChooseEffect(_ effect: Effect)
}

class EffectCell: UITableViewCell {
    @IBOutlet weak var customImageView: UIImageView!
}

class EffectListViewController: UITableViewController {
    
    weak var delegate: EffectListViewControllerDelegate? = nil
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section {
        case 0:
            return "Glasses"
        case 1:
            return "Mustaches"
        case 2:
            return "Lips"
        case _:
            fatalError("internal inconsistency")
        }
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 12
        case 1:
            return 4
        case 2:
            return 3
        case _:
            fatalError("internal inconsistency")
        }
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "effect", for: indexPath) as! EffectCell
        switch indexPath.section {
        case 0:
            cell.customImageView.image = UIImage(named: "glasses\(indexPath.row + 1)")!
        case 1:
            cell.customImageView.image = UIImage(named: "mustache\(indexPath.row + 1)")!
        case 2:
            cell.customImageView.image = UIImage(named: "lips\(indexPath.row + 1)")!
        case _:
            fatalError("internal inconsistency")
        }
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        var effect: Effect!
        
        switch indexPath.section {
        case 0:
            effect = Effect(type: .glasses, image: UIImage(named: "glasses\(indexPath.row + 1)")!)
        case 1:
            effect = Effect(type: .mustache, image: UIImage(named: "mustache\(indexPath.row + 1)")!)
        case 2:
            effect = Effect(type: .lips, image: UIImage(named: "lips\(indexPath.row + 1)")!)
        case _:
            fatalError("internal inconsistency")
        }
        
        delegate?.didChooseEffect(effect)
        navigationController?.popViewController(animated: true)
        
    }
    
}
